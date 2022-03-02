package net.runelite.client.plugins.adonaifisher;

import com.google.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.adonaifisher.FishIdentification;
import net.runelite.client.plugins.adonaifisher.AdonaiFisherConfig;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

import java.awt.*;

import static net.runelite.api.widgets.WidgetID.*;

public class AdonaiFisherOverlay extends WidgetItemOverlay
{
	private final AdonaiFisherConfig config;
	private final ItemManager itemManager;

	@Inject
	AdonaiFisherOverlay(AdonaiFisherConfig config, ItemManager itemManager)
	{
		this.config = config;
		this.itemManager = itemManager;
		showOnInventory();
		showOnBank();
		showOnInterfaces(
				KEPT_ON_DEATH_GROUP_ID,
				GUIDE_PRICE_GROUP_ID,
				LOOTING_BAG_GROUP_ID,
				SEED_BOX_GROUP_ID,
				KINGDOM_GROUP_ID
		);
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		FishIdentification iden = findFishIdentification(itemId);
		if (iden == null || !iden.type.enabled.test(config))
		{
			return;
		}

		graphics.setFont(FontManager.getRunescapeSmallFont());
		renderText(graphics, widgetItem.getCanvasBounds(), iden);
	}

	private void renderText(Graphics2D graphics, Rectangle bounds, FishIdentification iden)
	{
		final net.runelite.client.ui.overlay.components.TextComponent textComponent = new TextComponent();
		textComponent.setPosition(new Point(bounds.x - 1, bounds.y + bounds.height - 1));
		textComponent.setColor(config.textColor());
		switch (config.identificationType())
		{
			case SHORT:
				textComponent.setText(iden.shortName);
				break;
			case MEDIUM:
				textComponent.setText(iden.medName);
				break;
		}
		textComponent.render(graphics);
	}

	private FishIdentification findFishIdentification(final int itemID)
	{
		final int realItemId = itemManager.canonicalize(itemID);
		return FishIdentification.get(realItemId);
	}
}
