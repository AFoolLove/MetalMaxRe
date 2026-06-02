package me.afoolslove.metalmaxre.gamescript.i;

/**
 * 持有该接口时，会给予一个 block 属性，值为下一个代码结构的起始地址
 * 注：IBlock一定是ListScript
 */
public interface IBlock {
    String BLOCK = "block";

    /**
     * 获取所有长度，含opcode
     *
     * @return 长度
     */
    int argsLength();
}
