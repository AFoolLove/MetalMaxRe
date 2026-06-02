// Generated from C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/core/src/main/java/me/afoolslove/metalmaxre/gamescript/SpriteScript.g4 by ANTLR 4.13.2
package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SpriteScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface SpriteScriptVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#program}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitProgram(SpriteScriptParser.ProgramContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatement(SpriteScriptParser.StatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#typeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypeStatement(SpriteScriptParser.TypeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#henshinStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitHenshinStatement(SpriteScriptParser.HenshinStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#medalStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMedalStatement(SpriteScriptParser.MedalStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#codeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCodeStatement(SpriteScriptParser.CodeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifEventStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfEventStatement(SpriteScriptParser.IfEventStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotEventStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotEventStatement(SpriteScriptParser.IfNotEventStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifTeamStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfTeamStatement(SpriteScriptParser.IfTeamStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotTeamStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotTeamStatement(SpriteScriptParser.IfNotTeamStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifTeammateDeadStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfTeammateDeadStatement(SpriteScriptParser.IfTeammateDeadStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotTeammateDeadStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotTeammateDeadStatement(SpriteScriptParser.IfNotTeammateDeadStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifRideTankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfRideTankStatement(SpriteScriptParser.IfRideTankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotRideTankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotRideTankStatement(SpriteScriptParser.IfNotRideTankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifTankRidingStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfTankRidingStatement(SpriteScriptParser.IfTankRidingStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotTankRidingStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotTankRidingStatement(SpriteScriptParser.IfNotTankRidingStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifTankHereStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfTankHereStatement(SpriteScriptParser.IfTankHereStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotTankHereStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotTankHereStatement(SpriteScriptParser.IfNotTankHereStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifHasOkTankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfHasOkTankStatement(SpriteScriptParser.IfHasOkTankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotHasOkTankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotHasOkTankStatement(SpriteScriptParser.IfNotHasOkTankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifHasItemStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfHasItemStatement(SpriteScriptParser.IfHasItemStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotHasItemStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotHasItemStatement(SpriteScriptParser.IfNotHasItemStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifFaceStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfFaceStatement(SpriteScriptParser.IfFaceStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotFaceStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotFaceStatement(SpriteScriptParser.IfNotFaceStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifLevelStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfLevelStatement(SpriteScriptParser.IfLevelStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotLevelStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotLevelStatement(SpriteScriptParser.IfNotLevelStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifXyStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfXyStatement(SpriteScriptParser.IfXyStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotXyStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotXyStatement(SpriteScriptParser.IfNotXyStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifAreaStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfAreaStatement(SpriteScriptParser.IfAreaStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotAreaStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotAreaStatement(SpriteScriptParser.IfNotAreaStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifMoneyStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfMoneyStatement(SpriteScriptParser.IfMoneyStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotMoneyStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotMoneyStatement(SpriteScriptParser.IfNotMoneyStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifTreasureStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfTreasureStatement(SpriteScriptParser.IfTreasureStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotTreasureStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotTreasureStatement(SpriteScriptParser.IfNotTreasureStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifHpStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfHpStatement(SpriteScriptParser.IfHpStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotHpStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotHpStatement(SpriteScriptParser.IfNotHpStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifOptionYesStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfOptionYesStatement(SpriteScriptParser.IfOptionYesStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotOptionYesStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotOptionYesStatement(SpriteScriptParser.IfNotOptionYesStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifDetectorStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfDetectorStatement(SpriteScriptParser.IfDetectorStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotDetectorStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotDetectorStatement(SpriteScriptParser.IfNotDetectorStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifDrankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfDrankStatement(SpriteScriptParser.IfDrankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#ifNotDrankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfNotDrankStatement(SpriteScriptParser.IfNotDrankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#endifStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEndifStatement(SpriteScriptParser.EndifStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#labelTargetStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLabelTargetStatement(SpriteScriptParser.LabelTargetStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#doLoopStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDoLoopStatement(SpriteScriptParser.DoLoopStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcModelStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcModelStatement(SpriteScriptParser.NpcModelStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcModelTileTypeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcModelTileTypeStatement(SpriteScriptParser.NpcModelTileTypeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcActStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcActStatement(SpriteScriptParser.NpcActStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcAttrsStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcAttrsStatement(SpriteScriptParser.NpcAttrsStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcTankEnterStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcTankEnterStatement(SpriteScriptParser.NpcTankEnterStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcTankExitStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcTankExitStatement(SpriteScriptParser.NpcTankExitStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcPatrolStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcPatrolStatement(SpriteScriptParser.NpcPatrolStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcBecomeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcBecomeStatement(SpriteScriptParser.NpcBecomeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcOpenDoorStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcOpenDoorStatement(SpriteScriptParser.NpcOpenDoorStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcExplodeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcExplodeStatement(SpriteScriptParser.NpcExplodeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcHurtStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcHurtStatement(SpriteScriptParser.NpcHurtStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcDrawTileStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcDrawTileStatement(SpriteScriptParser.NpcDrawTileStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcHideStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcHideStatement(SpriteScriptParser.NpcHideStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcRemoveStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcRemoveStatement(SpriteScriptParser.NpcRemoveStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveStatement(SpriteScriptParser.NpcMoveStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveToStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveToStatement(SpriteScriptParser.NpcMoveToStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveTpStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveTpStatement(SpriteScriptParser.NpcMoveTpStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveChaseStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveChaseStatement(SpriteScriptParser.NpcMoveChaseStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveWanderStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveWanderStatement(SpriteScriptParser.NpcMoveWanderStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveTankStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveTankStatement(SpriteScriptParser.NpcMoveTankStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcMoveTpOffsetStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcMoveTpOffsetStatement(SpriteScriptParser.NpcMoveTpOffsetStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcAnimPlayStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcAnimPlayStatement(SpriteScriptParser.NpcAnimPlayStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcAnimFrameStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcAnimFrameStatement(SpriteScriptParser.NpcAnimFrameStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcAnimResumeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcAnimResumeStatement(SpriteScriptParser.NpcAnimResumeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#npcAnimThrowStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNpcAnimThrowStatement(SpriteScriptParser.NpcAnimThrowStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#moneySpendStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMoneySpendStatement(SpriteScriptParser.MoneySpendStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#givePlayerItemStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGivePlayerItemStatement(SpriteScriptParser.GivePlayerItemStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#tilesSpriteStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTilesSpriteStatement(SpriteScriptParser.TilesSpriteStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#swapPlayerItemStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSwapPlayerItemStatement(SpriteScriptParser.SwapPlayerItemStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#quakeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQuakeStatement(SpriteScriptParser.QuakeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#textPlainStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTextPlainStatement(SpriteScriptParser.TextPlainStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#textOptionStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTextOptionStatement(SpriteScriptParser.TextOptionStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#textQuoteStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTextQuoteStatement(SpriteScriptParser.TextQuoteStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#textEventStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTextEventStatement(SpriteScriptParser.TextEventStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#textBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTextBlock(SpriteScriptParser.TextBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#eventOpenStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEventOpenStatement(SpriteScriptParser.EventOpenStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#eventCloseStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEventCloseStatement(SpriteScriptParser.EventCloseStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#eventWaitStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEventWaitStatement(SpriteScriptParser.EventWaitStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#teamJoinStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTeamJoinStatement(SpriteScriptParser.TeamJoinStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#teamLeaveStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTeamLeaveStatement(SpriteScriptParser.TeamLeaveStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#teamHideStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTeamHideStatement(SpriteScriptParser.TeamHideStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#controlChangeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitControlChangeStatement(SpriteScriptParser.ControlChangeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#menuStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMenuStatement(SpriteScriptParser.MenuStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#jmpStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitJmpStatement(SpriteScriptParser.JmpStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#scrollStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitScrollStatement(SpriteScriptParser.ScrollStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#faceStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFaceStatement(SpriteScriptParser.FaceStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#faceBackStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFaceBackStatement(SpriteScriptParser.FaceBackStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#facePlayerStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFacePlayerStatement(SpriteScriptParser.FacePlayerStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerFaceStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerFaceStatement(SpriteScriptParser.PlayerFaceStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerShowStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerShowStatement(SpriteScriptParser.PlayerShowStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerShowCustomStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerShowCustomStatement(SpriteScriptParser.PlayerShowCustomStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerHideStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerHideStatement(SpriteScriptParser.PlayerHideStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerHideAllStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerHideAllStatement(SpriteScriptParser.PlayerHideAllStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerBecomeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerBecomeStatement(SpriteScriptParser.PlayerBecomeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerTpStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerTpStatement(SpriteScriptParser.PlayerTpStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerPullStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerPullStatement(SpriteScriptParser.PlayerPullStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#playerUnpullStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlayerUnpullStatement(SpriteScriptParser.PlayerUnpullStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#battleStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBattleStatement(SpriteScriptParser.BattleStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#waitTimeStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitWaitTimeStatement(SpriteScriptParser.WaitTimeStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#waitEventStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitWaitEventStatement(SpriteScriptParser.WaitEventStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#flashScreenStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFlashScreenStatement(SpriteScriptParser.FlashScreenStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#conveyorStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConveyorStatement(SpriteScriptParser.ConveyorStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#changeTankMapStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitChangeTankMapStatement(SpriteScriptParser.ChangeTankMapStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#recoverStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRecoverStatement(SpriteScriptParser.RecoverStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#sleepStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSleepStatement(SpriteScriptParser.SleepStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#musicStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMusicStatement(SpriteScriptParser.MusicStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#nextdayStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNextdayStatement(SpriteScriptParser.NextdayStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#speakerStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSpeakerStatement(SpriteScriptParser.SpeakerStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#sceneStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSceneStatement(SpriteScriptParser.SceneStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#systemStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSystemStatement(SpriteScriptParser.SystemStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#endStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEndStatement(SpriteScriptParser.EndStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link SpriteScriptParser#defineStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDefineStatement(SpriteScriptParser.DefineStatementContext ctx);
}