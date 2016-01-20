package mcjty.immcraft.input;


import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.network.PacketSendKey;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class InputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.keyNextItem.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_NEXTITEM));
        } else if (KeyBindings.keyPrevItem.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_PREVIOUSITEM));
        }
    }
}
