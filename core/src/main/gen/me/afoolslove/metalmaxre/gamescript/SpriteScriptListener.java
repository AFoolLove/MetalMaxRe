// Generated from C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/core/src/main/java/me/afoolslove/metalmaxre/gamescript/SpriteScript.g4 by ANTLR 4.13.2
package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SpriteScriptParser}.
 */
public interface SpriteScriptListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#program}.
     *
     * @param ctx the parse tree
     */
    void enterProgram(SpriteScriptParser.ProgramContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#program}.
     *
     * @param ctx the parse tree
     */
    void exitProgram(SpriteScriptParser.ProgramContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterStatement(SpriteScriptParser.StatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitStatement(SpriteScriptParser.StatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#typeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTypeStatement(SpriteScriptParser.TypeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#typeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTypeStatement(SpriteScriptParser.TypeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#henshinStatement}.
     *
     * @param ctx the parse tree
     */
    void enterHenshinStatement(SpriteScriptParser.HenshinStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#henshinStatement}.
     *
     * @param ctx the parse tree
     */
    void exitHenshinStatement(SpriteScriptParser.HenshinStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#medalStatement}.
     *
     * @param ctx the parse tree
     */
    void enterMedalStatement(SpriteScriptParser.MedalStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#medalStatement}.
     *
     * @param ctx the parse tree
     */
    void exitMedalStatement(SpriteScriptParser.MedalStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#codeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterCodeStatement(SpriteScriptParser.CodeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#codeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitCodeStatement(SpriteScriptParser.CodeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifEventStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfEventStatement(SpriteScriptParser.IfEventStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifEventStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfEventStatement(SpriteScriptParser.IfEventStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotEventStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotEventStatement(SpriteScriptParser.IfNotEventStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotEventStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotEventStatement(SpriteScriptParser.IfNotEventStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifTeamStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfTeamStatement(SpriteScriptParser.IfTeamStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifTeamStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfTeamStatement(SpriteScriptParser.IfTeamStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotTeamStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotTeamStatement(SpriteScriptParser.IfNotTeamStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotTeamStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotTeamStatement(SpriteScriptParser.IfNotTeamStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifTeammateDeadStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfTeammateDeadStatement(SpriteScriptParser.IfTeammateDeadStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifTeammateDeadStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfTeammateDeadStatement(SpriteScriptParser.IfTeammateDeadStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotTeammateDeadStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotTeammateDeadStatement(SpriteScriptParser.IfNotTeammateDeadStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotTeammateDeadStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotTeammateDeadStatement(SpriteScriptParser.IfNotTeammateDeadStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifRideTankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfRideTankStatement(SpriteScriptParser.IfRideTankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifRideTankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfRideTankStatement(SpriteScriptParser.IfRideTankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotRideTankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotRideTankStatement(SpriteScriptParser.IfNotRideTankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotRideTankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotRideTankStatement(SpriteScriptParser.IfNotRideTankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifTankRidingStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfTankRidingStatement(SpriteScriptParser.IfTankRidingStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifTankRidingStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfTankRidingStatement(SpriteScriptParser.IfTankRidingStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotTankRidingStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotTankRidingStatement(SpriteScriptParser.IfNotTankRidingStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotTankRidingStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotTankRidingStatement(SpriteScriptParser.IfNotTankRidingStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifTankHereStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfTankHereStatement(SpriteScriptParser.IfTankHereStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifTankHereStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfTankHereStatement(SpriteScriptParser.IfTankHereStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotTankHereStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotTankHereStatement(SpriteScriptParser.IfNotTankHereStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotTankHereStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotTankHereStatement(SpriteScriptParser.IfNotTankHereStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifHasOkTankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfHasOkTankStatement(SpriteScriptParser.IfHasOkTankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifHasOkTankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfHasOkTankStatement(SpriteScriptParser.IfHasOkTankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotHasOkTankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotHasOkTankStatement(SpriteScriptParser.IfNotHasOkTankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotHasOkTankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotHasOkTankStatement(SpriteScriptParser.IfNotHasOkTankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifHasItemStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfHasItemStatement(SpriteScriptParser.IfHasItemStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifHasItemStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfHasItemStatement(SpriteScriptParser.IfHasItemStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotHasItemStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotHasItemStatement(SpriteScriptParser.IfNotHasItemStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotHasItemStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotHasItemStatement(SpriteScriptParser.IfNotHasItemStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifFaceStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfFaceStatement(SpriteScriptParser.IfFaceStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifFaceStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfFaceStatement(SpriteScriptParser.IfFaceStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotFaceStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotFaceStatement(SpriteScriptParser.IfNotFaceStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotFaceStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotFaceStatement(SpriteScriptParser.IfNotFaceStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifLevelStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfLevelStatement(SpriteScriptParser.IfLevelStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifLevelStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfLevelStatement(SpriteScriptParser.IfLevelStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotLevelStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotLevelStatement(SpriteScriptParser.IfNotLevelStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotLevelStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotLevelStatement(SpriteScriptParser.IfNotLevelStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifXyStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfXyStatement(SpriteScriptParser.IfXyStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifXyStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfXyStatement(SpriteScriptParser.IfXyStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotXyStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotXyStatement(SpriteScriptParser.IfNotXyStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotXyStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotXyStatement(SpriteScriptParser.IfNotXyStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifAreaStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfAreaStatement(SpriteScriptParser.IfAreaStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifAreaStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfAreaStatement(SpriteScriptParser.IfAreaStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotAreaStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotAreaStatement(SpriteScriptParser.IfNotAreaStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotAreaStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotAreaStatement(SpriteScriptParser.IfNotAreaStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifMoneyStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfMoneyStatement(SpriteScriptParser.IfMoneyStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifMoneyStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfMoneyStatement(SpriteScriptParser.IfMoneyStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotMoneyStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotMoneyStatement(SpriteScriptParser.IfNotMoneyStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotMoneyStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotMoneyStatement(SpriteScriptParser.IfNotMoneyStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifTreasureStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfTreasureStatement(SpriteScriptParser.IfTreasureStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifTreasureStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfTreasureStatement(SpriteScriptParser.IfTreasureStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotTreasureStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotTreasureStatement(SpriteScriptParser.IfNotTreasureStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotTreasureStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotTreasureStatement(SpriteScriptParser.IfNotTreasureStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifHpStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfHpStatement(SpriteScriptParser.IfHpStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifHpStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfHpStatement(SpriteScriptParser.IfHpStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotHpStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotHpStatement(SpriteScriptParser.IfNotHpStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotHpStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotHpStatement(SpriteScriptParser.IfNotHpStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifOptionYesStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfOptionYesStatement(SpriteScriptParser.IfOptionYesStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifOptionYesStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfOptionYesStatement(SpriteScriptParser.IfOptionYesStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotOptionYesStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotOptionYesStatement(SpriteScriptParser.IfNotOptionYesStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotOptionYesStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotOptionYesStatement(SpriteScriptParser.IfNotOptionYesStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifDetectorStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfDetectorStatement(SpriteScriptParser.IfDetectorStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifDetectorStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfDetectorStatement(SpriteScriptParser.IfDetectorStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotDetectorStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotDetectorStatement(SpriteScriptParser.IfNotDetectorStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotDetectorStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotDetectorStatement(SpriteScriptParser.IfNotDetectorStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifDrankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfDrankStatement(SpriteScriptParser.IfDrankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifDrankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfDrankStatement(SpriteScriptParser.IfDrankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#ifNotDrankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterIfNotDrankStatement(SpriteScriptParser.IfNotDrankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#ifNotDrankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitIfNotDrankStatement(SpriteScriptParser.IfNotDrankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#endifStatement}.
     *
     * @param ctx the parse tree
     */
    void enterEndifStatement(SpriteScriptParser.EndifStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#endifStatement}.
     *
     * @param ctx the parse tree
     */
    void exitEndifStatement(SpriteScriptParser.EndifStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#labelTargetStatement}.
     *
     * @param ctx the parse tree
     */
    void enterLabelTargetStatement(SpriteScriptParser.LabelTargetStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#labelTargetStatement}.
     *
     * @param ctx the parse tree
     */
    void exitLabelTargetStatement(SpriteScriptParser.LabelTargetStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#doLoopStatement}.
     *
     * @param ctx the parse tree
     */
    void enterDoLoopStatement(SpriteScriptParser.DoLoopStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#doLoopStatement}.
     *
     * @param ctx the parse tree
     */
    void exitDoLoopStatement(SpriteScriptParser.DoLoopStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcModelStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcModelStatement(SpriteScriptParser.NpcModelStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcModelStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcModelStatement(SpriteScriptParser.NpcModelStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcModelTileTypeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcModelTileTypeStatement(SpriteScriptParser.NpcModelTileTypeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcModelTileTypeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcModelTileTypeStatement(SpriteScriptParser.NpcModelTileTypeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcActStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcActStatement(SpriteScriptParser.NpcActStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcActStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcActStatement(SpriteScriptParser.NpcActStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcAttrsStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcAttrsStatement(SpriteScriptParser.NpcAttrsStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcAttrsStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcAttrsStatement(SpriteScriptParser.NpcAttrsStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcTankEnterStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcTankEnterStatement(SpriteScriptParser.NpcTankEnterStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcTankEnterStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcTankEnterStatement(SpriteScriptParser.NpcTankEnterStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcTankExitStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcTankExitStatement(SpriteScriptParser.NpcTankExitStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcTankExitStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcTankExitStatement(SpriteScriptParser.NpcTankExitStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcPatrolStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcPatrolStatement(SpriteScriptParser.NpcPatrolStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcPatrolStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcPatrolStatement(SpriteScriptParser.NpcPatrolStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcBecomeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcBecomeStatement(SpriteScriptParser.NpcBecomeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcBecomeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcBecomeStatement(SpriteScriptParser.NpcBecomeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcOpenDoorStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcOpenDoorStatement(SpriteScriptParser.NpcOpenDoorStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcOpenDoorStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcOpenDoorStatement(SpriteScriptParser.NpcOpenDoorStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcExplodeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcExplodeStatement(SpriteScriptParser.NpcExplodeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcExplodeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcExplodeStatement(SpriteScriptParser.NpcExplodeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcHurtStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcHurtStatement(SpriteScriptParser.NpcHurtStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcHurtStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcHurtStatement(SpriteScriptParser.NpcHurtStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcDrawTileStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcDrawTileStatement(SpriteScriptParser.NpcDrawTileStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcDrawTileStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcDrawTileStatement(SpriteScriptParser.NpcDrawTileStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcHideStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcHideStatement(SpriteScriptParser.NpcHideStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcHideStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcHideStatement(SpriteScriptParser.NpcHideStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcRemoveStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcRemoveStatement(SpriteScriptParser.NpcRemoveStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcRemoveStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcRemoveStatement(SpriteScriptParser.NpcRemoveStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveStatement(SpriteScriptParser.NpcMoveStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveStatement(SpriteScriptParser.NpcMoveStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveToStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveToStatement(SpriteScriptParser.NpcMoveToStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveToStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveToStatement(SpriteScriptParser.NpcMoveToStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveTpStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveTpStatement(SpriteScriptParser.NpcMoveTpStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveTpStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveTpStatement(SpriteScriptParser.NpcMoveTpStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveChaseStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveChaseStatement(SpriteScriptParser.NpcMoveChaseStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveChaseStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveChaseStatement(SpriteScriptParser.NpcMoveChaseStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveWanderStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveWanderStatement(SpriteScriptParser.NpcMoveWanderStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveWanderStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveWanderStatement(SpriteScriptParser.NpcMoveWanderStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveTankStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveTankStatement(SpriteScriptParser.NpcMoveTankStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveTankStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveTankStatement(SpriteScriptParser.NpcMoveTankStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcMoveTpOffsetStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcMoveTpOffsetStatement(SpriteScriptParser.NpcMoveTpOffsetStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcMoveTpOffsetStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcMoveTpOffsetStatement(SpriteScriptParser.NpcMoveTpOffsetStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcAnimPlayStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcAnimPlayStatement(SpriteScriptParser.NpcAnimPlayStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcAnimPlayStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcAnimPlayStatement(SpriteScriptParser.NpcAnimPlayStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcAnimFrameStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcAnimFrameStatement(SpriteScriptParser.NpcAnimFrameStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcAnimFrameStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcAnimFrameStatement(SpriteScriptParser.NpcAnimFrameStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcAnimResumeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcAnimResumeStatement(SpriteScriptParser.NpcAnimResumeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcAnimResumeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcAnimResumeStatement(SpriteScriptParser.NpcAnimResumeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#npcAnimThrowStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNpcAnimThrowStatement(SpriteScriptParser.NpcAnimThrowStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#npcAnimThrowStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNpcAnimThrowStatement(SpriteScriptParser.NpcAnimThrowStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#moneySpendStatement}.
     *
     * @param ctx the parse tree
     */
    void enterMoneySpendStatement(SpriteScriptParser.MoneySpendStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#moneySpendStatement}.
     *
     * @param ctx the parse tree
     */
    void exitMoneySpendStatement(SpriteScriptParser.MoneySpendStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#givePlayerItemStatement}.
     *
     * @param ctx the parse tree
     */
    void enterGivePlayerItemStatement(SpriteScriptParser.GivePlayerItemStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#givePlayerItemStatement}.
     *
     * @param ctx the parse tree
     */
    void exitGivePlayerItemStatement(SpriteScriptParser.GivePlayerItemStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#tilesSpriteStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTilesSpriteStatement(SpriteScriptParser.TilesSpriteStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#tilesSpriteStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTilesSpriteStatement(SpriteScriptParser.TilesSpriteStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#swapPlayerItemStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSwapPlayerItemStatement(SpriteScriptParser.SwapPlayerItemStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#swapPlayerItemStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSwapPlayerItemStatement(SpriteScriptParser.SwapPlayerItemStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#quakeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterQuakeStatement(SpriteScriptParser.QuakeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#quakeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitQuakeStatement(SpriteScriptParser.QuakeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#textPlainStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTextPlainStatement(SpriteScriptParser.TextPlainStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#textPlainStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTextPlainStatement(SpriteScriptParser.TextPlainStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#textOptionStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTextOptionStatement(SpriteScriptParser.TextOptionStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#textOptionStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTextOptionStatement(SpriteScriptParser.TextOptionStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#textQuoteStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTextQuoteStatement(SpriteScriptParser.TextQuoteStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#textQuoteStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTextQuoteStatement(SpriteScriptParser.TextQuoteStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#textEventStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTextEventStatement(SpriteScriptParser.TextEventStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#textEventStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTextEventStatement(SpriteScriptParser.TextEventStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#textBlock}.
     *
     * @param ctx the parse tree
     */
    void enterTextBlock(SpriteScriptParser.TextBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#textBlock}.
     *
     * @param ctx the parse tree
     */
    void exitTextBlock(SpriteScriptParser.TextBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#eventOpenStatement}.
     *
     * @param ctx the parse tree
     */
    void enterEventOpenStatement(SpriteScriptParser.EventOpenStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#eventOpenStatement}.
     *
     * @param ctx the parse tree
     */
    void exitEventOpenStatement(SpriteScriptParser.EventOpenStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#eventCloseStatement}.
     *
     * @param ctx the parse tree
     */
    void enterEventCloseStatement(SpriteScriptParser.EventCloseStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#eventCloseStatement}.
     *
     * @param ctx the parse tree
     */
    void exitEventCloseStatement(SpriteScriptParser.EventCloseStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#eventWaitStatement}.
     *
     * @param ctx the parse tree
     */
    void enterEventWaitStatement(SpriteScriptParser.EventWaitStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#eventWaitStatement}.
     *
     * @param ctx the parse tree
     */
    void exitEventWaitStatement(SpriteScriptParser.EventWaitStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#teamJoinStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTeamJoinStatement(SpriteScriptParser.TeamJoinStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#teamJoinStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTeamJoinStatement(SpriteScriptParser.TeamJoinStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#teamLeaveStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTeamLeaveStatement(SpriteScriptParser.TeamLeaveStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#teamLeaveStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTeamLeaveStatement(SpriteScriptParser.TeamLeaveStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#teamHideStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTeamHideStatement(SpriteScriptParser.TeamHideStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#teamHideStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTeamHideStatement(SpriteScriptParser.TeamHideStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#controlChangeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterControlChangeStatement(SpriteScriptParser.ControlChangeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#controlChangeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitControlChangeStatement(SpriteScriptParser.ControlChangeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#menuStatement}.
     *
     * @param ctx the parse tree
     */
    void enterMenuStatement(SpriteScriptParser.MenuStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#menuStatement}.
     *
     * @param ctx the parse tree
     */
    void exitMenuStatement(SpriteScriptParser.MenuStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#jmpStatement}.
     *
     * @param ctx the parse tree
     */
    void enterJmpStatement(SpriteScriptParser.JmpStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#jmpStatement}.
     *
     * @param ctx the parse tree
     */
    void exitJmpStatement(SpriteScriptParser.JmpStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#scrollStatement}.
     *
     * @param ctx the parse tree
     */
    void enterScrollStatement(SpriteScriptParser.ScrollStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#scrollStatement}.
     *
     * @param ctx the parse tree
     */
    void exitScrollStatement(SpriteScriptParser.ScrollStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#faceStatement}.
     *
     * @param ctx the parse tree
     */
    void enterFaceStatement(SpriteScriptParser.FaceStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#faceStatement}.
     *
     * @param ctx the parse tree
     */
    void exitFaceStatement(SpriteScriptParser.FaceStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#faceBackStatement}.
     *
     * @param ctx the parse tree
     */
    void enterFaceBackStatement(SpriteScriptParser.FaceBackStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#faceBackStatement}.
     *
     * @param ctx the parse tree
     */
    void exitFaceBackStatement(SpriteScriptParser.FaceBackStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#facePlayerStatement}.
     *
     * @param ctx the parse tree
     */
    void enterFacePlayerStatement(SpriteScriptParser.FacePlayerStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#facePlayerStatement}.
     *
     * @param ctx the parse tree
     */
    void exitFacePlayerStatement(SpriteScriptParser.FacePlayerStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerFaceStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerFaceStatement(SpriteScriptParser.PlayerFaceStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerFaceStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerFaceStatement(SpriteScriptParser.PlayerFaceStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerShowStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerShowStatement(SpriteScriptParser.PlayerShowStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerShowStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerShowStatement(SpriteScriptParser.PlayerShowStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerShowCustomStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerShowCustomStatement(SpriteScriptParser.PlayerShowCustomStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerShowCustomStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerShowCustomStatement(SpriteScriptParser.PlayerShowCustomStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerHideStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerHideStatement(SpriteScriptParser.PlayerHideStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerHideStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerHideStatement(SpriteScriptParser.PlayerHideStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerHideAllStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerHideAllStatement(SpriteScriptParser.PlayerHideAllStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerHideAllStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerHideAllStatement(SpriteScriptParser.PlayerHideAllStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerBecomeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerBecomeStatement(SpriteScriptParser.PlayerBecomeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerBecomeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerBecomeStatement(SpriteScriptParser.PlayerBecomeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerTpStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerTpStatement(SpriteScriptParser.PlayerTpStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerTpStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerTpStatement(SpriteScriptParser.PlayerTpStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerPullStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerPullStatement(SpriteScriptParser.PlayerPullStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerPullStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerPullStatement(SpriteScriptParser.PlayerPullStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#playerUnpullStatement}.
     *
     * @param ctx the parse tree
     */
    void enterPlayerUnpullStatement(SpriteScriptParser.PlayerUnpullStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#playerUnpullStatement}.
     *
     * @param ctx the parse tree
     */
    void exitPlayerUnpullStatement(SpriteScriptParser.PlayerUnpullStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#battleStatement}.
     *
     * @param ctx the parse tree
     */
    void enterBattleStatement(SpriteScriptParser.BattleStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#battleStatement}.
     *
     * @param ctx the parse tree
     */
    void exitBattleStatement(SpriteScriptParser.BattleStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#waitTimeStatement}.
     *
     * @param ctx the parse tree
     */
    void enterWaitTimeStatement(SpriteScriptParser.WaitTimeStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#waitTimeStatement}.
     *
     * @param ctx the parse tree
     */
    void exitWaitTimeStatement(SpriteScriptParser.WaitTimeStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#waitEventStatement}.
     *
     * @param ctx the parse tree
     */
    void enterWaitEventStatement(SpriteScriptParser.WaitEventStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#waitEventStatement}.
     *
     * @param ctx the parse tree
     */
    void exitWaitEventStatement(SpriteScriptParser.WaitEventStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#flashScreenStatement}.
     *
     * @param ctx the parse tree
     */
    void enterFlashScreenStatement(SpriteScriptParser.FlashScreenStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#flashScreenStatement}.
     *
     * @param ctx the parse tree
     */
    void exitFlashScreenStatement(SpriteScriptParser.FlashScreenStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#conveyorStatement}.
     *
     * @param ctx the parse tree
     */
    void enterConveyorStatement(SpriteScriptParser.ConveyorStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#conveyorStatement}.
     *
     * @param ctx the parse tree
     */
    void exitConveyorStatement(SpriteScriptParser.ConveyorStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#changeTankMapStatement}.
     *
     * @param ctx the parse tree
     */
    void enterChangeTankMapStatement(SpriteScriptParser.ChangeTankMapStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#changeTankMapStatement}.
     *
     * @param ctx the parse tree
     */
    void exitChangeTankMapStatement(SpriteScriptParser.ChangeTankMapStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#recoverStatement}.
     *
     * @param ctx the parse tree
     */
    void enterRecoverStatement(SpriteScriptParser.RecoverStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#recoverStatement}.
     *
     * @param ctx the parse tree
     */
    void exitRecoverStatement(SpriteScriptParser.RecoverStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#sleepStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSleepStatement(SpriteScriptParser.SleepStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#sleepStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSleepStatement(SpriteScriptParser.SleepStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#musicStatement}.
     *
     * @param ctx the parse tree
     */
    void enterMusicStatement(SpriteScriptParser.MusicStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#musicStatement}.
     *
     * @param ctx the parse tree
     */
    void exitMusicStatement(SpriteScriptParser.MusicStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#nextdayStatement}.
     *
     * @param ctx the parse tree
     */
    void enterNextdayStatement(SpriteScriptParser.NextdayStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#nextdayStatement}.
     *
     * @param ctx the parse tree
     */
    void exitNextdayStatement(SpriteScriptParser.NextdayStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#speakerStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSpeakerStatement(SpriteScriptParser.SpeakerStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#speakerStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSpeakerStatement(SpriteScriptParser.SpeakerStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#sceneStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSceneStatement(SpriteScriptParser.SceneStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#sceneStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSceneStatement(SpriteScriptParser.SceneStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#systemStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSystemStatement(SpriteScriptParser.SystemStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#systemStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSystemStatement(SpriteScriptParser.SystemStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#endStatement}.
     *
     * @param ctx the parse tree
     */
    void enterEndStatement(SpriteScriptParser.EndStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#endStatement}.
     *
     * @param ctx the parse tree
     */
    void exitEndStatement(SpriteScriptParser.EndStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link SpriteScriptParser#defineStatement}.
     *
     * @param ctx the parse tree
     */
    void enterDefineStatement(SpriteScriptParser.DefineStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link SpriteScriptParser#defineStatement}.
     *
     * @param ctx the parse tree
     */
    void exitDefineStatement(SpriteScriptParser.DefineStatementContext ctx);
}