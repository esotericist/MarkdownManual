package li.cil.manual.client.document.segment;


import li.cil.manual.client.document.DocumentRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BoldSegment extends TextSegment {
    public BoldSegment(final DocumentRenderer document, final Segment parent, final String text) {
        super(document, parent, text);
    }

    // --------------------------------------------------------------------- //

    @Override
    public String toString() {
        return String.format("**%s**", super.toString());
    }

    // --------------------------------------------------------------------- //

    @Override
    protected String getFormat() {
        return super.getFormat() + TextFormatting.BOLD;
    }
}
