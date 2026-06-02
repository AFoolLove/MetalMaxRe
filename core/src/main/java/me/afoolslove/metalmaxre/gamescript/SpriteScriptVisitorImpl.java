package me.afoolslove.metalmaxre.gamescript;

import me.afoolslove.metalmaxre.gamescript.system.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Objects;

public class SpriteScriptVisitorImpl extends SpriteScriptBaseVisitor<BaseSpriteScript> {
    private boolean isDynamic = false;

    /**
     * 创建脚本并设置标签，返回脚本实例
     */
    private static <T extends BaseSpriteScript> T label(T script, TerminalNode label) {
        script.setLabel(label);
        return script;
    }

    /**
     * 获取参数，忽略label影响的位置偏移
     */
    private static ParseTree getArg(ParserRuleContext ctx, int index) {
        TerminalNode label = ctx.getToken(SpriteScriptParser.LABEL, 0);
        if (index == 0) {
            return label;
        }
        if (label == null) {
            index--;
        }
        return ctx.getChild(index);
    }

    @Override
    public BaseSpriteScript visitProgram(SpriteScriptParser.ProgramContext ctx) {
        return new RootScript(ctx, this);
    }


    @Override
    public BaseSpriteScript visitEndifStatement(SpriteScriptParser.EndifStatementContext ctx) {
        return new ListScript.EndIfScript((TerminalNode) ctx.getChild(0));
    }


    @Override
    public BaseSpriteScript visitTypeStatement(SpriteScriptParser.TypeStatementContext ctx) {
        ParseTree child = ctx.getChild(1);
        if (child != null && child.getText().equals("static")) {
            isDynamic = false;
            return TypeScript.STATIC;
        } else {
            isDynamic = true;
            return TypeScript.DYNAMIC;
        }
    }

    @Override
    public BaseSpriteScript visitHenshinStatement(SpriteScriptParser.HenshinStatementContext ctx) {
        return label(new HenshinScript(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitMedalStatement(SpriteScriptParser.MedalStatementContext ctx) {
        return label(new MedalScript(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitDefineStatement(SpriteScriptParser.DefineStatementContext ctx) {
        return new EmptyScript();
    }

    @Override
    public BaseSpriteScript visitCodeStatement(SpriteScriptParser.CodeStatementContext ctx) {
        return label(new CodeScript(ctx.BYTE()), ctx.LABEL());
    }

    // ========== if 语句 ==========

    @Override
    public BaseSpriteScript visitIfEventStatement(SpriteScriptParser.IfEventStatementContext ctx) {
        IfScript.Event ifScript = new IfScript.Event(ctx.HEX(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfTeamStatement(SpriteScriptParser.IfTeamStatementContext ctx) {
        IfScript.Team ifScript = new IfScript.Team(ctx.HEX(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfTeammateDeadStatement(SpriteScriptParser.IfTeammateDeadStatementContext ctx) {
        IfScript.TeammateDead ifScript = new IfScript.TeammateDead(ctx.HEX(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfRideTankStatement(SpriteScriptParser.IfRideTankStatementContext ctx) {
        IfScript.RideTank ifScript = new IfScript.RideTank(ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfTankRidingStatement(SpriteScriptParser.IfTankRidingStatementContext ctx) {
        IfScript.Tank.Riding ifScript = new IfScript.Tank.Riding(ctx.HEX(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfTankHereStatement(SpriteScriptParser.IfTankHereStatementContext ctx) {
        IfScript.Tank.Here ifScript = new IfScript.Tank.Here(ctx.HEX(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfHasOkTankStatement(SpriteScriptParser.IfHasOkTankStatementContext ctx) {
        IfScript.HasOkTank ifScript = new IfScript.HasOkTank(ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfHasItemStatement(SpriteScriptParser.IfHasItemStatementContext ctx) {
        IfScript.HasItem ifScript = new IfScript.HasItem(ctx.HEX(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfFaceStatement(SpriteScriptParser.IfFaceStatementContext ctx) {
        String faceStr = switch (ctx.DIRECTION().getText()) {
            case "up" -> "00";
            case "down" -> "01";
            case "left" -> "02";
            case "right" -> "03";
            default -> throw new IllegalArgumentException("Invalid direction: " + ctx.DIRECTION().getText());
        };
        IfScript.Face ifScript = new IfScript.Face(faceStr, ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfLevelStatement(SpriteScriptParser.IfLevelStatementContext ctx) {
        IfScript.Level ifScript = new IfScript.Level(ctx.NUMBER(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfXyStatement(SpriteScriptParser.IfXyStatementContext ctx) {
        IfScript.Xy ifScript = new IfScript.Xy(ctx.BYTE(0), ctx.BYTE(1), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfAreaStatement(SpriteScriptParser.IfAreaStatementContext ctx) {
        IfScript.Area ifScript = new IfScript.Area(ctx.BYTE(0), ctx.BYTE(1), ctx.BYTE(2), ctx.BYTE(3), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfMoneyStatement(SpriteScriptParser.IfMoneyStatementContext ctx) {
        IfScript.Money ifScript = new IfScript.Money(ctx.NUMBER(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfTreasureStatement(SpriteScriptParser.IfTreasureStatementContext ctx) {
        IfScript.Treasure ifScript = new IfScript.Treasure(ctx.BYTE(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfHpStatement(SpriteScriptParser.IfHpStatementContext ctx) {
        IfScript.Hp ifScript = new IfScript.Hp(ctx.NUMBER(), ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfOptionYesStatement(SpriteScriptParser.IfOptionYesStatementContext ctx) {
        IfScript.Option.Yes ifScript = new IfScript.Option.Yes(ctx, this);
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfDetectorStatement(SpriteScriptParser.IfDetectorStatementContext ctx) {
        DetectorScript detectorScript = new DetectorScript(ctx.NUMBER(), ctx, this);
        return label(detectorScript, ctx.LABEL());
    }

    // ========== ifnot (判断失败跳转) 语句 ==========

    @Override
    public BaseSpriteScript visitLabelTargetStatement(SpriteScriptParser.LabelTargetStatementContext ctx) {
        return new ListScript.LabelTargetScript(ctx.STRING());
    }

    @Override
    public BaseSpriteScript visitIfNotEventStatement(SpriteScriptParser.IfNotEventStatementContext ctx) {
        IfScript.Event ifScript = new IfScript.Event(ctx.HEX());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotTeamStatement(SpriteScriptParser.IfNotTeamStatementContext ctx) {
        IfScript.Team ifScript = new IfScript.Team(ctx.HEX());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotTeammateDeadStatement(SpriteScriptParser.IfNotTeammateDeadStatementContext ctx) {
        IfScript.TeammateDead ifScript = new IfScript.TeammateDead(ctx.HEX());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotRideTankStatement(SpriteScriptParser.IfNotRideTankStatementContext ctx) {
        IfScript.RideTank ifScript = new IfScript.RideTank();
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotTankRidingStatement(SpriteScriptParser.IfNotTankRidingStatementContext ctx) {
        IfScript.Tank.Riding ifScript = new IfScript.Tank.Riding(ctx.HEX());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotTankHereStatement(SpriteScriptParser.IfNotTankHereStatementContext ctx) {
        IfScript.Tank.Here ifScript = new IfScript.Tank.Here(ctx.HEX());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotHasOkTankStatement(SpriteScriptParser.IfNotHasOkTankStatementContext ctx) {
        IfScript.HasOkTank ifScript = new IfScript.HasOkTank();
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotHasItemStatement(SpriteScriptParser.IfNotHasItemStatementContext ctx) {
        IfScript.HasItem ifScript = new IfScript.HasItem(ctx.HEX());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotFaceStatement(SpriteScriptParser.IfNotFaceStatementContext ctx) {
        String faceStr = switch (ctx.DIRECTION().getText()) {
            case "up" -> "00";
            case "down" -> "01";
            case "left" -> "02";
            case "right" -> "03";
            default -> throw new IllegalArgumentException("Invalid direction: " + ctx.DIRECTION().getText());
        };
        IfScript.Face ifScript = new IfScript.Face(faceStr);
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotLevelStatement(SpriteScriptParser.IfNotLevelStatementContext ctx) {
        IfScript.Level ifScript = new IfScript.Level(ctx.NUMBER());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotXyStatement(SpriteScriptParser.IfNotXyStatementContext ctx) {
        IfScript.Xy ifScript = new IfScript.Xy(ctx.BYTE(0), ctx.BYTE(1));
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotAreaStatement(SpriteScriptParser.IfNotAreaStatementContext ctx) {
        IfScript.Area ifScript = new IfScript.Area(ctx.BYTE(0), ctx.BYTE(1), ctx.BYTE(2), ctx.BYTE(3));
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotMoneyStatement(SpriteScriptParser.IfNotMoneyStatementContext ctx) {
        IfScript.Money ifScript = new IfScript.Money(ctx.NUMBER());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotTreasureStatement(SpriteScriptParser.IfNotTreasureStatementContext ctx) {
        IfScript.Treasure ifScript = new IfScript.Treasure(ctx.BYTE());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotHpStatement(SpriteScriptParser.IfNotHpStatementContext ctx) {
        IfScript.Hp ifScript = new IfScript.Hp(ctx.NUMBER());
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotOptionYesStatement(SpriteScriptParser.IfNotOptionYesStatementContext ctx) {
        IfScript.Option.Yes ifScript = new IfScript.Option.Yes();
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotDetectorStatement(SpriteScriptParser.IfNotDetectorStatementContext ctx) {
        DetectorScript detectorScript = new DetectorScript(ctx.NUMBER(), ctx, this);
        detectorScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(detectorScript, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfNotDrankStatement(SpriteScriptParser.IfNotDrankStatementContext ctx) {
        IfScript.Drank ifScript = new IfScript.Drank(ctx, this);
        ifScript.setTargetLabel(ctx.labelTargetStatement().STRING().getText());
        return label(ifScript, ctx.LABEL());
    }

    // ========== 循环 / NPC模型 / 队伍 ==========

    @Override
    public BaseSpriteScript visitDoLoopStatement(SpriteScriptParser.DoLoopStatementContext ctx) {
        return label(new LoopScript(ctx.NUMBER(), ctx, this), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcModelStatement(SpriteScriptParser.NpcModelStatementContext ctx) {
        if (ctx.BYTE() == null) {
            return switch (getArg(ctx, 2).getText()) {
                case "previous" -> label(new NpcScript.Model.Previous(), ctx.LABEL());
                case "next" -> label(new NpcScript.Model.Next(), ctx.LABEL());
                default -> throw new IllegalArgumentException("Unknown model keyword: " + getArg(ctx, 2).getText());
            };
        }
        return label(new NpcScript.Model.Set(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcModelTileTypeStatement(SpriteScriptParser.NpcModelTileTypeStatementContext ctx) {
        return label(new NpcScript.Model.TileType(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTeamJoinStatement(SpriteScriptParser.TeamJoinStatementContext ctx) {
        boolean now = Objects.equals(getArg(ctx, 2).getText(), "now");
        if (now) {
            return label(new TeamScript.Join.Now(ctx.HEX()), ctx.LABEL());
        }
        return label(new TeamScript.Join(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTeamLeaveStatement(SpriteScriptParser.TeamLeaveStatementContext ctx) {
        return label(new TeamScript.Leave(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTeamHideStatement(SpriteScriptParser.TeamHideStatementContext ctx) {
        return label(new TeamScript.Hide(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitIfDrankStatement(SpriteScriptParser.IfDrankStatementContext ctx) {
        return label(new IfScript.Drank(ctx, this), ctx.LABEL());
    }

    // ========== 文本 / 事件 / 菜单 / 移除 ==========

    @Override
    public BaseSpriteScript visitTextPlainStatement(SpriteScriptParser.TextPlainStatementContext ctx) {
        List<String> text = ctx.textBlock().TEXT().stream().map(TerminalNode::getText).toList();
        return label(new TextScript.Plain(text), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTextOptionStatement(SpriteScriptParser.TextOptionStatementContext ctx) {
        List<String> text = ctx.textBlock().TEXT().stream().map(TerminalNode::getText).toList();
        return label(new TextScript.Option(text), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTextQuoteStatement(SpriteScriptParser.TextQuoteStatementContext ctx) {
        return label(new TextScript.Quote(isDynamic, ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTextEventStatement(SpriteScriptParser.TextEventStatementContext ctx) {
        return label(new TextScript.Event(ctx.BYTE(0), ctx.BYTE(1), ctx.BYTE(2), ctx.BYTE(3)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitEventOpenStatement(SpriteScriptParser.EventOpenStatementContext ctx) {
        return label(new EventScript.Open(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitEventCloseStatement(SpriteScriptParser.EventCloseStatementContext ctx) {
        return label(new EventScript.Close(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitEventWaitStatement(SpriteScriptParser.EventWaitStatementContext ctx) {
        return label(new EventScript.Wait(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitControlChangeStatement(SpriteScriptParser.ControlChangeStatementContext ctx) {
        return label(new ControlScript.Change(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitMenuStatement(SpriteScriptParser.MenuStatementContext ctx) {
        return label(new MenuScript(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitJmpStatement(SpriteScriptParser.JmpStatementContext ctx) {
        return label(new JmpScript(ctx.STRING().getText()), ctx.LABEL());
    }

    // ========== NPC移动 ==========

    @Override
    public BaseSpriteScript visitNpcMoveStatement(SpriteScriptParser.NpcMoveStatementContext ctx) {
        NpcScript.MoveScript.Direction direction = switch (ctx.DIRECTION().getText()) {
            case "up" -> new NpcScript.MoveScript.Up();
            case "down" -> new NpcScript.MoveScript.Down();
            case "left" -> new NpcScript.MoveScript.Left();
            case "right" -> new NpcScript.MoveScript.Right();
            default -> throw new IllegalArgumentException("Invalid direction: " + ctx.DIRECTION().getText());
        };
        label(direction, ctx.LABEL());
        if (ctx.NUMBER() != null) {
            direction.count = BaseSpriteScript.number(ctx.NUMBER());
        }
        return direction;
    }

    @Override
    public BaseSpriteScript visitNpcMoveToStatement(SpriteScriptParser.NpcMoveToStatementContext ctx) {
        return label(new NpcScript.MoveScript.To(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcMoveTpStatement(SpriteScriptParser.NpcMoveTpStatementContext ctx) {
        return label(new NpcScript.MoveScript.Tp(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }


    @Override
    public BaseSpriteScript visitNpcMoveTpOffsetStatement(SpriteScriptParser.NpcMoveTpOffsetStatementContext ctx) {
        return label(new NpcScript.MoveScript.TpOffset(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcMoveChaseStatement(SpriteScriptParser.NpcMoveChaseStatementContext ctx) {
        NpcScript.MoveScript.Chase moveChase = label(new NpcScript.MoveScript.Chase(), ctx.LABEL());
        if (ctx.NUMBER() != null) {
            moveChase.count = BaseSpriteScript.number(ctx.NUMBER());
        }
        return moveChase;
    }

    @Override
    public BaseSpriteScript visitNpcMoveWanderStatement(SpriteScriptParser.NpcMoveWanderStatementContext ctx) {
        NpcScript.MoveScript.Wander wander = !ctx.BYTE().isEmpty()
                ? new NpcScript.MoveScript.Wander(ctx.BYTE(0), ctx.BYTE(1), ctx.BYTE(2), ctx.BYTE(3))
                : new NpcScript.MoveScript.Wander();
        return label(wander, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcMoveTankStatement(SpriteScriptParser.NpcMoveTankStatementContext ctx) {
        return label(new NpcScript.MoveScript.Tank(), ctx.LABEL());
    }

    // ========== 滚动 / 面向 ==========

    @Override
    public BaseSpriteScript visitScrollStatement(SpriteScriptParser.ScrollStatementContext ctx) {
        return switch (ctx.DIRECTION().getText()) {
            case "up" -> new ScrollScript.Up(ctx.NUMBER());
            case "down" -> new ScrollScript.Down(ctx.NUMBER());
            case "left" -> new ScrollScript.Left(ctx.NUMBER());
            case "right" -> new ScrollScript.Right(ctx.NUMBER());
            default -> throw new IllegalArgumentException("Invalid direction: " + ctx.DIRECTION().getText());
        };
    }

    @Override
    public BaseSpriteScript visitFaceStatement(SpriteScriptParser.FaceStatementContext ctx) {
        return label(switch (ctx.DIRECTION().getText()) {
            case "up" -> new NpcScript.FaceScript.Up();
            case "down" -> new NpcScript.FaceScript.Down();
            case "left" -> new NpcScript.FaceScript.Left();
            case "right" -> new NpcScript.FaceScript.Right();
            default -> throw new IllegalArgumentException("Invalid direction: " + ctx.DIRECTION().getText());
        }, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitFaceBackStatement(SpriteScriptParser.FaceBackStatementContext ctx) {
        return label(new NpcScript.FaceScript.Back(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitFacePlayerStatement(SpriteScriptParser.FacePlayerStatementContext ctx) {
        return label(new NpcScript.FaceScript.Player(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerFaceStatement(SpriteScriptParser.PlayerFaceStatementContext ctx) {
        if (ctx.DIRECTION() != null) {
            return label(switch (ctx.DIRECTION().getText()) {
                case "up" -> new PlayerScript.Face.Up();
                case "down" -> new PlayerScript.Face.Down();
                case "left" -> new PlayerScript.Face.Left();
                case "right" -> new PlayerScript.Face.Right();
                default -> throw new IllegalArgumentException("Invalid direction: " + ctx.DIRECTION().getText());
            }, ctx.LABEL());
        } else {
            return label(new PlayerScript.Face(ctx.BYTE()), ctx.LABEL());
        }
    }

    // ========== 玩家操作 ==========

    @Override
    public BaseSpriteScript visitPlayerShowStatement(SpriteScriptParser.PlayerShowStatementContext ctx) {
        return label(new PlayerScript.Show(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerShowCustomStatement(SpriteScriptParser.PlayerShowCustomStatementContext ctx) {
        return label(new PlayerScript.Show(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerHideStatement(SpriteScriptParser.PlayerHideStatementContext ctx) {
        return label(new PlayerScript.Hide(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerHideAllStatement(SpriteScriptParser.PlayerHideAllStatementContext ctx) {
        return label(new PlayerScript.Hide.All(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerBecomeStatement(SpriteScriptParser.PlayerBecomeStatementContext ctx) {
        return label(new PlayerScript.Become(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerTpStatement(SpriteScriptParser.PlayerTpStatementContext ctx) {
        if (ctx.BYTE().isEmpty()) {
            return label(new PlayerScript.Tp(), ctx.LABEL());
        }
        return label(new PlayerScript.Tp(ctx.BYTE(0), ctx.BYTE(1), ctx.BYTE(2), getArg(ctx, 5) != null), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerPullStatement(SpriteScriptParser.PlayerPullStatementContext ctx) {
        return label(new PlayerScript.Pull(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitPlayerUnpullStatement(SpriteScriptParser.PlayerUnpullStatementContext ctx) {
        return label(new PlayerScript.Unpull(), ctx.LABEL());
    }

    // ========== 战斗 / NPC行为 ==========

    @Override
    public BaseSpriteScript visitBattleStatement(SpriteScriptParser.BattleStatementContext ctx) {
        return label(new BattleScript(ctx.BYTE(0), ctx.BYTE(1), ctx.BYTE(2)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcActStatement(SpriteScriptParser.NpcActStatementContext ctx) {
        return label(new NpcScript.Act(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcAttrsStatement(SpriteScriptParser.NpcAttrsStatementContext ctx) {
        return label(new NpcScript.Attrs(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcTankEnterStatement(SpriteScriptParser.NpcTankEnterStatementContext ctx) {
        return label(new NpcScript.EnterTank(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcTankExitStatement(SpriteScriptParser.NpcTankExitStatementContext ctx) {
        return label(new NpcScript.ExitTank(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcPatrolStatement(SpriteScriptParser.NpcPatrolStatementContext ctx) {
        return label(new NpcScript.Patrol(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcBecomeStatement(SpriteScriptParser.NpcBecomeStatementContext ctx) {
        return label(new NpcScript.Become(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcOpenDoorStatement(SpriteScriptParser.NpcOpenDoorStatementContext ctx) {
        return label(new NpcScript.OpenDoor(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcExplodeStatement(SpriteScriptParser.NpcExplodeStatementContext ctx) {
        return label(new NpcScript.Explode(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcHurtStatement(SpriteScriptParser.NpcHurtStatementContext ctx) {
        return label(new NpcScript.Hurt(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcDrawTileStatement(SpriteScriptParser.NpcDrawTileStatementContext ctx) {
        return label(new NpcScript.DrawTile(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcHideStatement(SpriteScriptParser.NpcHideStatementContext ctx) {
        return label(new NpcScript.HideScript(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcRemoveStatement(SpriteScriptParser.NpcRemoveStatementContext ctx) {
        return label(new NpcScript.RemoveScript(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcAnimPlayStatement(SpriteScriptParser.NpcAnimPlayStatementContext ctx) {
        return label(new NpcScript.Anim.Play(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcAnimFrameStatement(SpriteScriptParser.NpcAnimFrameStatementContext ctx) {
        return label(new NpcScript.Anim.Frame(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcAnimResumeStatement(SpriteScriptParser.NpcAnimResumeStatementContext ctx) {
        return label(new NpcScript.Anim.Resume(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNpcAnimThrowStatement(SpriteScriptParser.NpcAnimThrowStatementContext ctx) {
        return label(new NpcScript.Anim.Throw(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitMoneySpendStatement(SpriteScriptParser.MoneySpendStatementContext ctx) {
        return label(new MoneyScript.Spend(ctx.NUMBER()), ctx.LABEL());
    }

    // ========== 物品 / 地图瓦片 / 交换 ==========

    @Override
    public BaseSpriteScript visitGivePlayerItemStatement(SpriteScriptParser.GivePlayerItemStatementContext ctx) {
        return label(new GiveScript.Player.Item(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitTilesSpriteStatement(SpriteScriptParser.TilesSpriteStatementContext ctx) {
        return label(new TilesScript.Sprite(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitSwapPlayerItemStatement(SpriteScriptParser.SwapPlayerItemStatementContext ctx) {
        return label(new SwapScript.Player.Item(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    // ========== 特效 / 等待 / 场景等 ==========

    @Override
    public BaseSpriteScript visitQuakeStatement(SpriteScriptParser.QuakeStatementContext ctx) {
        return label(new QuakeScript(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitWaitTimeStatement(SpriteScriptParser.WaitTimeStatementContext ctx) {
        TerminalNode time = ctx.BYTE() != null ? ctx.BYTE() : (TerminalNode) getArg(ctx, 2);
        return label(new WaitScript.Time(time), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitWaitEventStatement(SpriteScriptParser.WaitEventStatementContext ctx) {
        return label(new WaitScript.Event(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitFlashScreenStatement(SpriteScriptParser.FlashScreenStatementContext ctx) {
        return label(new FlashScreenScript(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitConveyorStatement(SpriteScriptParser.ConveyorStatementContext ctx) {
        return label(new ConveyorScript((TerminalNode) getArg(ctx, 2)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitChangeTankMapStatement(SpriteScriptParser.ChangeTankMapStatementContext ctx) {
        return label(new ChangeTankMapScript(ctx.BYTE(0), ctx.BYTE(1)), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitRecoverStatement(SpriteScriptParser.RecoverStatementContext ctx) {
        return label(new RecoverScript(ctx.HEX()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitSleepStatement(SpriteScriptParser.SleepStatementContext ctx) {
        return label(new SleepScript(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitMusicStatement(SpriteScriptParser.MusicStatementContext ctx) {
        return label(new MusicScript(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitNextdayStatement(SpriteScriptParser.NextdayStatementContext ctx) {
        return label(new NextDayScript(), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitSpeakerStatement(SpriteScriptParser.SpeakerStatementContext ctx) {
        return label(new SpeakerScript(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitSceneStatement(SpriteScriptParser.SceneStatementContext ctx) {
        SceneScript scene = ctx.BYTE() == null ? new SceneScript.End() : new SceneScript(ctx.BYTE());
        return label(scene, ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitSystemStatement(SpriteScriptParser.SystemStatementContext ctx) {
        return label(new SystemScript(ctx.BYTE()), ctx.LABEL());
    }

    @Override
    public BaseSpriteScript visitEndStatement(SpriteScriptParser.EndStatementContext ctx) {
        return label(new EndScript(), ctx.LABEL());
    }

}
