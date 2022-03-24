package net.runelite.client.plugins.adonaibanker;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.function.Predicate;

enum BankIdentification
{
	// Other
	BANKER(Type.BANKER, "Banker", "B", 1, 2, 3),
	BANK_BOOTH(Type.BANKER, "Banker", "BB", 1, 2, 3);

	final Type type;
	final String medName;
	final String shortName;
	final int[] itemIDs;

	BankIdentification(Type type, String medName, String shortName, int... ids)
	{
		this.type = type;
		this.medName = medName;
		this.shortName = shortName;
		this.itemIDs = ids;
	}

	private static final Map<Integer, BankIdentification> itemIdentifications;

	static
	{
		ImmutableMap.Builder<Integer, BankIdentification> builder = new ImmutableMap.Builder<>();

		for (BankIdentification i : values())
		{
			for (int id : i.itemIDs)
			{
				builder.put(id, i);
			}
		}

		itemIdentifications = builder.build();
	}

	static BankIdentification get(int id)
	{
		return itemIdentifications.get(id);
	}

	@AllArgsConstructor
	enum Type
	{
//		BANKER(AdonaiBankerConfig::useBanker),
//		BANK_BOOTH(AdonaiBankerConfig::useBankBooth),
//		DEPOSIT_BOX(AdonaiBankerConfig::useBankDeposit);
		BANKER(AdonaiBankerConfig::useBanker),
		BANK_BOOTH(AdonaiBankerConfig::useBankBooth),
		DEPOSIT_BOX(AdonaiBankerConfig::useBankDeposit);

		final Predicate<AdonaiBankerConfig> enabled;
	}
}
