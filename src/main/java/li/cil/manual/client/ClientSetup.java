package li.cil.manual.client;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.ManualScreenStyle;
import li.cil.manual.api.ManualStyle;
import li.cil.manual.api.Tab;
import li.cil.manual.api.provider.DocumentProvider;
import li.cil.manual.api.provider.PathProvider;
import li.cil.manual.api.provider.RendererProvider;
import li.cil.manual.api.util.Constants;
import li.cil.manual.api.util.ShowManualScreenEvent;
import li.cil.manual.client.gui.ManualScreen;
import li.cil.manual.client.provider.BlockRendererProvider;
import li.cil.manual.client.provider.ItemRendererProvider;
import li.cil.manual.client.provider.TagRendererProvider;
import li.cil.manual.client.provider.TextureRendererProvider;
import li.cil.manual.client.util.RegistryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

@OnlyIn(Dist.CLIENT)
public final class ClientSetup {
    public static void initialize() {
        RegistryUtils.begin(Constants.MOD_ID);

        final DeferredRegister<Tab> tabs = RegistryUtils.create(Tab.class);
        final DeferredRegister<PathProvider> pathProviders = RegistryUtils.create(PathProvider.class);
        final DeferredRegister<DocumentProvider> documentProviders = RegistryUtils.create(DocumentProvider.class);
        final DeferredRegister<RendererProvider> rendererProviders = RegistryUtils.create(RendererProvider.class);
        final DeferredRegister<ManualModel> manuals = RegistryUtils.create(ManualModel.class);

        makeClientOnlyRegistry(pathProviders, Constants.PATH_PROVIDERS);
        makeClientOnlyRegistry(documentProviders, Constants.DOCUMENT_PROVIDERS);
        makeClientOnlyRegistry(rendererProviders, Constants.RENDERER_PROVIDERS);
        makeClientOnlyRegistry(tabs, Constants.TABS);
        makeClientOnlyRegistry(manuals, Constants.MANUALS);

        rendererProviders.register("texture", TextureRendererProvider::new);
        rendererProviders.register("item", ItemRendererProvider::new);
        rendererProviders.register("block", BlockRendererProvider::new);
        rendererProviders.register("tag", TagRendererProvider::new);

        RegistryUtils.finish();

        MinecraftForge.EVENT_BUS.addListener(ClientSetup::handleShowManualScreen);
    }

    // --------------------------------------------------------------------- //

    private static void handleShowManualScreen(final ShowManualScreenEvent event) {
        final ManualModel model = event.getManualModel();
        final ManualStyle manualStyle = event.getManualStyle().orElse(ManualStyle.DEFAULT);
        final ManualScreenStyle screenStyle = event.getScreenStyle().orElse(ManualScreenStyle.DEFAULT);

        final ManualScreen screen = new ManualScreen(model, manualStyle, screenStyle);
        Minecraft.getInstance().setScreen(screen);
    }

    private static <T extends IForgeRegistryEntry<T>> void makeClientOnlyRegistry(final DeferredRegister<T> deferredRegister, final RegistryKey<Registry<T>> key) {
        deferredRegister.makeRegistry(key.location().getPath(), () -> new RegistryBuilder<T>().disableSync().disableSaving());
    }
}
