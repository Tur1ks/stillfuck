package my.stur1ks.legit;

import my.stur1ks.legit.module.Stealler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("legit")
public class Legit {
    public Legit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(Stealler.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
}
