package carpetextra.mixins;

import carpetextra.CarpetExtraSettings;
import carpetextra.helpers.CarpetDispenserBehaviours.*;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import carpetextra.utils.PlaceBlockDispenserBehavior;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin
{
    @Shadow @Final private static Map<Item, DispenserBehavior> BEHAVIORS;

    @Inject(method = "getBehaviorForItem", at = @At("HEAD"), cancellable = true)
    private void getBehaviorForItem(ItemStack itemStack_1, CallbackInfoReturnable<DispenserBehavior> cir)
    {
        Item item = itemStack_1.getItem();
        if (CarpetExtraSettings.dispenserPlacesBlocks && !BEHAVIORS.containsKey(item) && item instanceof BlockItem)
        {
            if (PlaceBlockDispenserBehavior.canPlace(((BlockItem) item).getBlock()))
            {
                cir.setReturnValue(PlaceBlockDispenserBehavior.getInstance());
                cir.cancel();
            }
        }
        if (item == Items.GLASS_BOTTLE)
            cir.setReturnValue(new WaterBottleDispenserBehaviour());
        
        if (item == Items.CHEST)
            cir.setReturnValue(new MinecartDispenserBehaviour(AbstractMinecartEntity.Type.CHEST));
        
        if (item == Items.HOPPER)
            cir.setReturnValue(new MinecartDispenserBehaviour(AbstractMinecartEntity.Type.HOPPER));
        
        if (item == Items.FURNACE)
            cir.setReturnValue(new MinecartDispenserBehaviour(AbstractMinecartEntity.Type.FURNACE));
        
        if (item == Items.TNT)
            cir.setReturnValue(new MinecartDispenserBehaviour(AbstractMinecartEntity.Type.TNT));
        
        if (item instanceof MusicDiscItem)
            cir.setReturnValue(new DispenserRecords());
        
        if (item instanceof HoeItem)
            cir.setReturnValue(new TillSoilDispenserBehaviour());
        
    }
}
