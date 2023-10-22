package com.arcomit.sub.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

/***
 * 延迟获取值
 * 请使用{}创造子类去捕获父类泛型
 * @author til
 */
public abstract class Delayed<E> {
    public Supplier<E> supplier;
    public E e;

    public Delayed(Supplier<E> supplier) {
        this.supplier = supplier;
    }

    public E get() {
        if (e == null) {
            if (supplier != null) {
                e = supplier.get();
            }
            supplier = null;
        }
        return e;
    }

    public static class ItemStackDelayed extends Delayed<ItemStack> {
        public ItemStackDelayed(Supplier<ItemStack> supplier) {
            super(supplier);
        }
    }

    public static class FluidStackDelayed extends Delayed<FluidStack> {
        public FluidStackDelayed(Supplier<FluidStack> supplier) {
            super(supplier);
        }
    }

    public static class BlockStateDelayed extends Delayed<BlockState> {
        public BlockStateDelayed(Supplier<BlockState> supplier) {
            super(supplier);
        }
    }

}
