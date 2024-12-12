package me.afoolslove.metalmaxre.editors.sprite.script;

import com.google.gson.annotations.SerializedName;

public class SpriteScriptAction {
    @SerializedName("code")
    private int code;
    @SerializedName("type")
    private SpriteScriptActionType type = SpriteScriptActionType.GENERAL_TYPE;
    @SerializedName("name")
    private String name;
    @SerializedName("length")
    private int length = 0;
    @SerializedName("format")
    private String format;
    @SerializedName("formatText")
    private String formatText;
    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("detailedDescription")
    private String detailedDescription;
    @SerializedName("gotoIndex")
    private Integer gotoIndex;
    @SerializedName("notLeaf")
    private Boolean notLeaf;

    public SpriteScriptAction(int code) {
        this(code, 0x00);
    }

    public SpriteScriptAction(int code, int length) {
        this(code, length, "未知命令", "unknown");
    }

    public SpriteScriptAction(int code, int length, String name) {
        this(code, length, name, "unknown");
    }

    public SpriteScriptAction(int code, int length, String name, String shortDescription) {
        this(code, length, name, shortDescription, shortDescription);
    }

    public SpriteScriptAction(int code, int length, String name, String shortDescription, String detailedDescription) {
        this.code = code;
        this.length = length;
        this.name = name;
        this.shortDescription = shortDescription;
        this.detailedDescription = detailedDescription;
    }

    /**
     * 获取对应的指令代码
     *
     * @return 对应的指令代码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取该指令适用的范围
     *
     * @return 该指令适用的范围
     */
    public SpriteScriptActionType getType() {
        return type;
    }

    /**
     * 获取指令名称
     *
     * @return 指令名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取该指令的参数长度
     *
     * @return 该指令的参数长度
     */
    public int getLength() {
        return length;
    }

    /**
     * 获取是否为空参数指令
     *
     * @return 是否为空参数指令
     */
    public boolean isEmptyArgs() {
        return getLength() == 0;
    }

    /**
     * 获取使用该指令的格式
     *
     * @return 使用该指令的格式
     */
    public String getFormat() {
        return format;
    }

    /**
     * 获取该指令的格式化文本
     *
     * @return 该指令的格式化文本
     */
    public String getFormatText() {
        return formatText;
    }

    /**
     * 获取简短描述
     *
     * @return 简短描述
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * 获取详细的描述
     *
     * @return 详细的描述及
     */
    public String getDetailedDescription() {
        return detailedDescription;
    }

    public Integer getGotoIndex() {
        return gotoIndex;
    }

    public Boolean getNotLeaf() {
        return notLeaf;
    }

    public boolean isLeaf() {
        return notLeaf != null && !notLeaf;
    }

    /**
     * 设置该指令的代码
     *
     * @param code 该指令的代码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 设置该指令适用的范围
     *
     * @param type 该指令适用的范围
     */
    public void setType(SpriteScriptActionType type) {
        this.type = type;
    }

    /**
     * 设置该指令的名称
     *
     * @param name 该指令的名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置该指令的参数长度
     *
     * @param length 该指令的参数长度
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * 设置使用该指令的格式
     *
     * @param format 使用该指令的格式
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 设置该指令格式化的文本
     *
     * @param formatText 该指令格式化的文本
     */
    public void setFormatText(String formatText) {
        this.formatText = formatText;
    }

    /**
     * 设置该指令的一行描述
     *
     * @param shortDescription 该指令的一行描述
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * 设置该指令的详细描述
     *
     * @param detailedDescription 该指令的详细描述
     */
    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }


    public void setGotoIndex(Integer gotoIndex) {
        this.gotoIndex = gotoIndex;
    }

    public void setNotLeaf(Boolean notLeaf) {
        this.notLeaf = notLeaf;
    }
}
