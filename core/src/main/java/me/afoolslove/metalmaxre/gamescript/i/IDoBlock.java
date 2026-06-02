package me.afoolslove.metalmaxre.gamescript.i;

/**
 * 持有该接口时，会给予一个 block 属性，值为下一个代码结构的起始地址
 * 注：IBlock一定是ListScript
 * 与 {@code IBlock}不同的是，代码块在该指令之前，类似于 do while
 */
public interface IDoBlock extends IBlock {
}
