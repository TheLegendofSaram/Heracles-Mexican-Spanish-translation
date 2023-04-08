package earth.terrarium.heracles.resource;

import com.google.common.collect.BiMap;
import earth.terrarium.heracles.Quest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.PredicateManager;

import java.util.Map;

public class QuestManager extends CodecResourceReloader<Quest> {
    private static QuestManager instance;

    public QuestManager(PredicateManager predicateManager) {
        super("quests/quest", Quest::codec, predicateManager);

        instance = this;
    }

    public static QuestManager getInstance() {
        return instance;
    }

    public BiMap<ResourceLocation, Quest> getQuests() {
        return getValues();
    }
}
