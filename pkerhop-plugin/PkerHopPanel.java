package net.runelite.client.plugins.pkerhop;

import com.google.common.collect.Ordering;
import lombok.AccessLevel;
import lombok.Setter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PkerHopPanel extends PluginPanel {
    private static final Color ODD_ROW = new Color(44, 44, 44);

    private static final int WORLD_COLUMN_WIDTH = 60;
    private static final int PLAYERS_COLUMN_WIDTH = 40;
    private static final int PING_COLUMN_WIDTH = 47;

    private final JPanel listContainer = new JPanel();

    private WorldTableHeader worldHeader;
    private WorldTableHeader playersHeader;
    private WorldTableHeader activityHeader;
    private WorldTableHeader pingHeader;

    private PkerHopPanel.WorldOrder orderIndex = PkerHopPanel.WorldOrder.WORLD;
    private boolean ascendingOrder = true;

    private final ArrayList<WorldTableRow> rows = new ArrayList<>();
    private final PkerHopPlugin plugin;
    @Setter(AccessLevel.PACKAGE)
    private SubscriptionFilterMode subscriptionFilterMode;
    @Setter(AccessLevel.PACKAGE)
    private RegionFilterMode regionFilterMode;

    PkerHopPanel(PkerHopPlugin plugin)
    {
        this.plugin = plugin;

        setBorder(null);
        setLayout(new DynamicGridLayout(0, 1));

        JPanel headerContainer = buildHeader();

        listContainer.setLayout(new GridLayout(0, 1));

        add(headerContainer);
        add(listContainer);
    }

    void switchCurrentHighlight(int newWorld, int lastWorld)
    {
        for (WorldTableRow row : rows)
        {
            if (row.getWorld().getId() == newWorld)
            {
                row.recolour(true);
            }
            else if (row.getWorld().getId() == lastWorld)
            {
                row.recolour(false);
            }
        }
    }

    void updateListData(Map<Integer, Integer> worldData)
    {
        for (WorldTableRow worldTableRow : rows)
        {
            World world = worldTableRow.getWorld();
            Integer playerCount = worldData.get(world.getId());
            if (playerCount != null)
            {
                worldTableRow.updatePlayerCount(playerCount);
            }
        }

        // If the list is being ordered by player count, then it has to be re-painted
        // to properly display the new data
        if (orderIndex == PkerHopPanel.WorldOrder.PLAYERS)
        {
            updateList();
        }
    }

    void updatePing(int world, int ping)
    {
        for (WorldTableRow worldTableRow : rows)
        {
            if (worldTableRow.getWorld().getId() == world)
            {
                worldTableRow.setPing(ping);

                // If the panel is sorted by ping, re-sort it
                if (orderIndex == PkerHopPanel.WorldOrder.PING)
                {
                    updateList();
                }
                break;
            }
        }
    }

    void hidePing()
    {
        for (WorldTableRow worldTableRow : rows)
        {
            worldTableRow.hidePing();
        }
    }

    void showPing()
    {
        for (WorldTableRow worldTableRow : rows)
        {
            worldTableRow.showPing();
        }
    }

    void updateList()
    {
        rows.sort((r1, r2) ->
        {
            switch (orderIndex)
            {
                case PING:
                    // Leave worlds with unknown ping at the bottom
                    return getCompareValue(r1, r2, row ->
                    {
                        int ping = row.getPing();
                        return ping > 0 ? ping : null;
                    });
                case WORLD:
                    return getCompareValue(r1, r2, row -> row.getWorld().getId());
                case PLAYERS:
                    return getCompareValue(r1, r2, WorldTableRow::getUpdatedPlayerCount);
                case ACTIVITY:
                    // Leave empty activity worlds on the bottom of the list
                    return getCompareValue(r1, r2, row ->
                    {
                        String activity = row.getWorld().getActivity();
                        return !activity.equals("-") ? activity : null;
                    });
                default:
                    return 0;
            }
        });

        rows.sort((r1, r2) ->
        {
            boolean b1 = plugin.isFavorite(r1.getWorld());
            boolean b2 = plugin.isFavorite(r2.getWorld());
            return Boolean.compare(b2, b1);
        });

        listContainer.removeAll();

        for (int i = 0; i < rows.size(); i++)
        {
            WorldTableRow row = rows.get(i);
            row.setBackground(i % 2 == 0 ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
            listContainer.add(row);
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private int getCompareValue(WorldTableRow row1, WorldTableRow row2, Function<WorldTableRow, Comparable> compareByFn)
    {
        Ordering<Comparable> ordering = Ordering.natural();
        if (!ascendingOrder)
        {
            ordering = ordering.reverse();
        }
        ordering = ordering.nullsLast();
        return ordering.compare(compareByFn.apply(row1), compareByFn.apply(row2));
    }

    void updateFavoriteMenu(int world, boolean favorite)
    {
        for (WorldTableRow row : rows)
        {
            if (row.getWorld().getId() == world)
            {
                row.setFavoriteMenu(favorite);
            }
        }
    }

    void populate(List<World> worlds)
    {
        rows.clear();

        for (int i = 0; i < worlds.size(); i++)
        {
            World world = worlds.get(i);

            switch (subscriptionFilterMode)
            {
                case FREE:
                    if (world.getTypes().contains(WorldType.MEMBERS))
                    {
                        continue;
                    }
                    break;
                case MEMBERS:
                    if (!world.getTypes().contains(WorldType.MEMBERS))
                    {
                        continue;
                    }
                    break;
            }

            if (regionFilterMode.getRegion() != null && !regionFilterMode.getRegion().equals(world.getRegion()))
            {
                continue;
            }

            rows.add(buildRow(world, i % 2 == 0, world.getId() == plugin.getCurrentWorld() && plugin.getLastWorld() != 0, plugin.isFavorite(world)));
        }

        updateList();
    }

    private void orderBy(PkerHopPanel.WorldOrder order)
    {
        pingHeader.highlight(false, ascendingOrder);
        worldHeader.highlight(false, ascendingOrder);
        playersHeader.highlight(false, ascendingOrder);
        activityHeader.highlight(false, ascendingOrder);

        switch (order)
        {
            case PING:
                pingHeader.highlight(true, ascendingOrder);
                break;
            case WORLD:
                worldHeader.highlight(true, ascendingOrder);
                break;
            case PLAYERS:
                playersHeader.highlight(true, ascendingOrder);
                break;
            case ACTIVITY:
                activityHeader.highlight(true, ascendingOrder);
                break;
        }

        orderIndex = order;
        updateList();
    }

    /**
     * Builds the entire table header.
     */
    private JPanel buildHeader()
    {
        JPanel header = new JPanel(new BorderLayout());
        JPanel leftSide = new JPanel(new BorderLayout());
        JPanel rightSide = new JPanel(new BorderLayout());

        pingHeader = new WorldTableHeader("Ping", orderIndex == PkerHopPanel.WorldOrder.PING, ascendingOrder, plugin::refresh);
        pingHeader.setPreferredSize(new Dimension(PING_COLUMN_WIDTH, 0));
        pingHeader.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent mouseEvent)
            {
                if (SwingUtilities.isRightMouseButton(mouseEvent))
                {
                    return;
                }
                ascendingOrder = orderIndex != PkerHopPanel.WorldOrder.PING || !ascendingOrder;
                orderBy(PkerHopPanel.WorldOrder.PING);
            }
        });

        worldHeader = new WorldTableHeader("World", orderIndex == PkerHopPanel.WorldOrder.WORLD, ascendingOrder, plugin::refresh);
        worldHeader.setPreferredSize(new Dimension(WORLD_COLUMN_WIDTH, 0));
        worldHeader.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent mouseEvent)
            {
                if (SwingUtilities.isRightMouseButton(mouseEvent))
                {
                    return;
                }
                ascendingOrder = orderIndex != PkerHopPanel.WorldOrder.WORLD || !ascendingOrder;
                orderBy(PkerHopPanel.WorldOrder.WORLD);
            }
        });

        playersHeader = new WorldTableHeader("#", orderIndex == PkerHopPanel.WorldOrder.PLAYERS, ascendingOrder, plugin::refresh);
        playersHeader.setPreferredSize(new Dimension(PLAYERS_COLUMN_WIDTH, 0));
        playersHeader.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent mouseEvent)
            {
                if (SwingUtilities.isRightMouseButton(mouseEvent))
                {
                    return;
                }
                ascendingOrder = orderIndex != PkerHopPanel.WorldOrder.PLAYERS || !ascendingOrder;
                orderBy(PkerHopPanel.WorldOrder.PLAYERS);
            }
        });

        activityHeader = new WorldTableHeader("Activity", orderIndex == PkerHopPanel.WorldOrder.ACTIVITY, ascendingOrder, plugin::refresh);
        activityHeader.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent mouseEvent)
            {
                if (SwingUtilities.isRightMouseButton(mouseEvent))
                {
                    return;
                }
                ascendingOrder = orderIndex != PkerHopPanel.WorldOrder.ACTIVITY || !ascendingOrder;
                orderBy(PkerHopPanel.WorldOrder.ACTIVITY);
            }
        });

        leftSide.add(worldHeader, BorderLayout.WEST);
        leftSide.add(playersHeader, BorderLayout.CENTER);

        rightSide.add(activityHeader, BorderLayout.CENTER);
        rightSide.add(pingHeader, BorderLayout.EAST);

        header.add(leftSide, BorderLayout.WEST);
        header.add(rightSide, BorderLayout.CENTER);

        return header;
    }

    /**
     * Builds a table row, that displays the world's information.
     */
    private WorldTableRow buildRow(World world, boolean stripe, boolean current, boolean favorite)
    {
        WorldTableRow row = new WorldTableRow(world, current, favorite, plugin.getStoredPing(world),
                plugin::hopTo,
                (world12, add) ->
                {
                    if (add)
                    {
                        plugin.addToFavorites(world12);
                    }
                    else
                    {
                        plugin.removeFromFavorites(world12);
                    }

                    updateList();
                }
        );
        row.setBackground(stripe ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
        return row;
    }

    /**
     * Enumerates the multiple ordering options for the world list.
     */
    private enum WorldOrder
    {
        WORLD,
        PLAYERS,
        ACTIVITY,
        PING
    }
}
