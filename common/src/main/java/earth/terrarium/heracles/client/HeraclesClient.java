package earth.terrarium.heracles.client;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.heracles.Heracles;
import earth.terrarium.heracles.client.handlers.DisplayConfig;
import earth.terrarium.heracles.client.handlers.QuestTutorial;
import earth.terrarium.heracles.client.screens.QuestTutorialScreen;
import earth.terrarium.heracles.client.toasts.QuestClaimedToast;
import earth.terrarium.heracles.client.toasts.QuestCompletedToast;
import earth.terrarium.heracles.common.network.NetworkHandler;
import earth.terrarium.heracles.common.network.packets.groups.OpenGroupPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;

import java.util.List;

public class HeraclesClient {

    public static final KeyMapping OPEN_QUESTS = new KeyMapping(
        "key.heracles.open_quests",
        InputConstants.KEY_U,
        "key.categories.odyssey"
    );

    public static void init() {
        Heracles.setRegistryAccess(() -> {
            var connection = Minecraft.getInstance().getConnection();
            if (connection == null) return RegistryAccess.EMPTY;
            return connection.registryAccess();
        });
    }

    public static void clientTick() {
        if (OPEN_QUESTS.consumeClick()) {
            openQuestScreen();
        }
        QuestTutorial.tick();
    }

    public static void openQuestScreen() {
        if (DisplayConfig.showTutorial) {
            if (!QuestTutorial.tutorialText().isBlank()) {
                Minecraft.getInstance().setScreen(new QuestTutorialScreen());
            } else {
                DisplayConfig.showTutorial = false;
                DisplayConfig.save();
                NetworkHandler.CHANNEL.sendToServer(new OpenGroupPacket("", false));
            }
        } else {
            NetworkHandler.CHANNEL.sendToServer(new OpenGroupPacket("", false));
        }
    }

    public static void displayItemsRewardedToast(String id, List<Item> items) {
        QuestClaimedToast.addOrUpdate(Minecraft.getInstance().getToasts(), id, items);
    }

    public static void displayQuestCompleteToast(String id) {
        QuestCompletedToast.add(Minecraft.getInstance().getToasts(), id);
    }
}
