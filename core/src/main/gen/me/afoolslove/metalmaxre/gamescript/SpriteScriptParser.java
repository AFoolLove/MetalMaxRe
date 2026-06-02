// Generated from C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/core/src/main/java/me/afoolslove/metalmaxre/gamescript/SpriteScript.g4 by ANTLR 4.13.2
package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SpriteScriptParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, T__15 = 16, T__16 = 17,
            T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24,
            T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28, T__28 = 29, T__29 = 30, T__30 = 31,
            T__31 = 32, T__32 = 33, T__33 = 34, T__34 = 35, T__35 = 36, T__36 = 37, T__37 = 38,
            T__38 = 39, T__39 = 40, T__40 = 41, T__41 = 42, T__42 = 43, T__43 = 44, T__44 = 45,
            T__45 = 46, T__46 = 47, T__47 = 48, T__48 = 49, T__49 = 50, T__50 = 51, T__51 = 52,
            T__52 = 53, T__53 = 54, T__54 = 55, T__55 = 56, T__56 = 57, T__57 = 58, T__58 = 59,
            T__59 = 60, T__60 = 61, T__61 = 62, T__62 = 63, T__63 = 64, T__64 = 65, T__65 = 66,
            T__66 = 67, T__67 = 68, T__68 = 69, T__69 = 70, T__70 = 71, T__71 = 72, T__72 = 73,
            T__73 = 74, T__74 = 75, T__75 = 76, T__76 = 77, T__77 = 78, T__78 = 79, T__79 = 80,
            T__80 = 81, T__81 = 82, T__82 = 83, T__83 = 84, T__84 = 85, T__85 = 86, T__86 = 87,
            T__87 = 88, T__88 = 89, T__89 = 90, T__90 = 91, T__91 = 92, T__92 = 93, T__93 = 94,
            T__94 = 95, T__95 = 96, T__96 = 97, T__97 = 98, T__98 = 99, T__99 = 100, T__100 = 101,
            T__101 = 102, T__102 = 103, T__103 = 104, T__104 = 105, T__105 = 106, T__106 = 107,
            T__107 = 108, T__108 = 109, T__109 = 110, T__110 = 111, T__111 = 112, T__112 = 113,
            T__113 = 114, T__114 = 115, T__115 = 116, T__116 = 117, T__117 = 118, T__118 = 119,
            T__119 = 120, T__120 = 121, T__121 = 122, T__122 = 123, T__123 = 124, T__124 = 125,
            T__125 = 126, T__126 = 127, T__127 = 128, DIRECTION = 129, LABEL = 130, LINE_COMMENT = 131,
            BLOCK_COMMENT = 132, TEXT = 133, STRING = 134, HEX = 135, BYTE = 136, BYTES = 137,
            NUMBER = 138, WS = 139;
    public static final int
            RULE_program = 0, RULE_statement = 1, RULE_typeStatement = 2, RULE_henshinStatement = 3,
            RULE_medalStatement = 4, RULE_codeStatement = 5, RULE_ifEventStatement = 6,
            RULE_ifNotEventStatement = 7, RULE_ifTeamStatement = 8, RULE_ifNotTeamStatement = 9,
            RULE_ifTeammateDeadStatement = 10, RULE_ifNotTeammateDeadStatement = 11,
            RULE_ifRideTankStatement = 12, RULE_ifNotRideTankStatement = 13, RULE_ifTankRidingStatement = 14,
            RULE_ifNotTankRidingStatement = 15, RULE_ifTankHereStatement = 16, RULE_ifNotTankHereStatement = 17,
            RULE_ifHasOkTankStatement = 18, RULE_ifNotHasOkTankStatement = 19, RULE_ifHasItemStatement = 20,
            RULE_ifNotHasItemStatement = 21, RULE_ifFaceStatement = 22, RULE_ifNotFaceStatement = 23,
            RULE_ifLevelStatement = 24, RULE_ifNotLevelStatement = 25, RULE_ifXyStatement = 26,
            RULE_ifNotXyStatement = 27, RULE_ifAreaStatement = 28, RULE_ifNotAreaStatement = 29,
            RULE_ifMoneyStatement = 30, RULE_ifNotMoneyStatement = 31, RULE_ifTreasureStatement = 32,
            RULE_ifNotTreasureStatement = 33, RULE_ifHpStatement = 34, RULE_ifNotHpStatement = 35,
            RULE_ifOptionYesStatement = 36, RULE_ifNotOptionYesStatement = 37, RULE_ifDetectorStatement = 38,
            RULE_ifNotDetectorStatement = 39, RULE_ifDrankStatement = 40, RULE_ifNotDrankStatement = 41,
            RULE_endifStatement = 42, RULE_labelTargetStatement = 43, RULE_doLoopStatement = 44,
            RULE_npcModelStatement = 45, RULE_npcModelTileTypeStatement = 46, RULE_npcActStatement = 47,
            RULE_npcAttrsStatement = 48, RULE_npcTankEnterStatement = 49, RULE_npcTankExitStatement = 50,
            RULE_npcPatrolStatement = 51, RULE_npcBecomeStatement = 52, RULE_npcOpenDoorStatement = 53,
            RULE_npcExplodeStatement = 54, RULE_npcHurtStatement = 55, RULE_npcDrawTileStatement = 56,
            RULE_npcHideStatement = 57, RULE_npcRemoveStatement = 58, RULE_npcMoveStatement = 59,
            RULE_npcMoveToStatement = 60, RULE_npcMoveTpStatement = 61, RULE_npcMoveChaseStatement = 62,
            RULE_npcMoveWanderStatement = 63, RULE_npcMoveTankStatement = 64, RULE_npcMoveTpOffsetStatement = 65,
            RULE_npcAnimPlayStatement = 66, RULE_npcAnimFrameStatement = 67, RULE_npcAnimResumeStatement = 68,
            RULE_npcAnimThrowStatement = 69, RULE_moneySpendStatement = 70, RULE_givePlayerItemStatement = 71,
            RULE_tilesSpriteStatement = 72, RULE_swapPlayerItemStatement = 73, RULE_quakeStatement = 74,
            RULE_textPlainStatement = 75, RULE_textOptionStatement = 76, RULE_textQuoteStatement = 77,
            RULE_textEventStatement = 78, RULE_textBlock = 79, RULE_eventOpenStatement = 80,
            RULE_eventCloseStatement = 81, RULE_eventWaitStatement = 82, RULE_teamJoinStatement = 83,
            RULE_teamLeaveStatement = 84, RULE_teamHideStatement = 85, RULE_controlChangeStatement = 86,
            RULE_menuStatement = 87, RULE_jmpStatement = 88, RULE_scrollStatement = 89,
            RULE_faceStatement = 90, RULE_faceBackStatement = 91, RULE_facePlayerStatement = 92,
            RULE_playerFaceStatement = 93, RULE_playerShowStatement = 94, RULE_playerShowCustomStatement = 95,
            RULE_playerHideStatement = 96, RULE_playerHideAllStatement = 97, RULE_playerBecomeStatement = 98,
            RULE_playerTpStatement = 99, RULE_playerPullStatement = 100, RULE_playerUnpullStatement = 101,
            RULE_battleStatement = 102, RULE_waitTimeStatement = 103, RULE_waitEventStatement = 104,
            RULE_flashScreenStatement = 105, RULE_conveyorStatement = 106, RULE_changeTankMapStatement = 107,
            RULE_recoverStatement = 108, RULE_sleepStatement = 109, RULE_musicStatement = 110,
            RULE_nextdayStatement = 111, RULE_speakerStatement = 112, RULE_sceneStatement = 113,
            RULE_systemStatement = 114, RULE_endStatement = 115, RULE_defineStatement = 116;

    private static String[] makeRuleNames() {
        return new String[]{
                "program", "statement", "typeStatement", "henshinStatement", "medalStatement",
                "codeStatement", "ifEventStatement", "ifNotEventStatement", "ifTeamStatement",
                "ifNotTeamStatement", "ifTeammateDeadStatement", "ifNotTeammateDeadStatement",
                "ifRideTankStatement", "ifNotRideTankStatement", "ifTankRidingStatement",
                "ifNotTankRidingStatement", "ifTankHereStatement", "ifNotTankHereStatement",
                "ifHasOkTankStatement", "ifNotHasOkTankStatement", "ifHasItemStatement",
                "ifNotHasItemStatement", "ifFaceStatement", "ifNotFaceStatement", "ifLevelStatement",
                "ifNotLevelStatement", "ifXyStatement", "ifNotXyStatement", "ifAreaStatement",
                "ifNotAreaStatement", "ifMoneyStatement", "ifNotMoneyStatement", "ifTreasureStatement",
                "ifNotTreasureStatement", "ifHpStatement", "ifNotHpStatement", "ifOptionYesStatement",
                "ifNotOptionYesStatement", "ifDetectorStatement", "ifNotDetectorStatement",
                "ifDrankStatement", "ifNotDrankStatement", "endifStatement", "labelTargetStatement",
                "doLoopStatement", "npcModelStatement", "npcModelTileTypeStatement",
                "npcActStatement", "npcAttrsStatement", "npcTankEnterStatement", "npcTankExitStatement",
                "npcPatrolStatement", "npcBecomeStatement", "npcOpenDoorStatement", "npcExplodeStatement",
                "npcHurtStatement", "npcDrawTileStatement", "npcHideStatement", "npcRemoveStatement",
                "npcMoveStatement", "npcMoveToStatement", "npcMoveTpStatement", "npcMoveChaseStatement",
                "npcMoveWanderStatement", "npcMoveTankStatement", "npcMoveTpOffsetStatement",
                "npcAnimPlayStatement", "npcAnimFrameStatement", "npcAnimResumeStatement",
                "npcAnimThrowStatement", "moneySpendStatement", "givePlayerItemStatement",
                "tilesSpriteStatement", "swapPlayerItemStatement", "quakeStatement",
                "textPlainStatement", "textOptionStatement", "textQuoteStatement", "textEventStatement",
                "textBlock", "eventOpenStatement", "eventCloseStatement", "eventWaitStatement",
                "teamJoinStatement", "teamLeaveStatement", "teamHideStatement", "controlChangeStatement",
                "menuStatement", "jmpStatement", "scrollStatement", "faceStatement",
                "faceBackStatement", "facePlayerStatement", "playerFaceStatement", "playerShowStatement",
                "playerShowCustomStatement", "playerHideStatement", "playerHideAllStatement",
                "playerBecomeStatement", "playerTpStatement", "playerPullStatement",
                "playerUnpullStatement", "battleStatement", "waitTimeStatement", "waitEventStatement",
                "flashScreenStatement", "conveyorStatement", "changeTankMapStatement",
                "recoverStatement", "sleepStatement", "musicStatement", "nextdayStatement",
                "speakerStatement", "sceneStatement", "systemStatement", "endStatement",
                "defineStatement"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "'#type'", "'static'", "'dynamic'", "'#henshin'", "'#medal'", "'#code'",
                "'#if event'", "'#ifnot event'", "'#if team'", "'#ifnot team'", "'#if teammate dead'",
                "'#ifnot teammate dead'", "'#if ridetank'", "'#ifnot ridetank'", "'#if tank riding'",
                "'#ifnot tank riding'", "'#if tank here'", "'#ifnot tank here'", "'#if hasoktank'",
                "'#ifnot hasoktank'", "'#if hasitem'", "'#ifnot hasitem'", "'#if face'",
                "'#ifnot face'", "'#if level'", "'#ifnot level'", "'#if xy'", "'#ifnot xy'",
                "'#if area'", "'#ifnot area'", "'#if money'", "'#ifnot money'", "'#if treasure'",
                "'#ifnot treasure'", "'#if hp'", "'#ifnot hp'", "'#if option yes'", "'#ifnot option yes'",
                "'#if detector'", "'#ifnot detector'", "'#if drank'", "'#ifnot drank'",
                "'#endif'", "'#loopif'", "'#do'", "'#loop'", "'#npc model'", "'previous'",
                "'next'", "'#npc model tiletype'", "'#npc act'", "'#npc attrs'", "'#npc tank enter'",
                "'#npc tank exit'", "'#npc patrol'", "'#npc become'", "'#npc opendoor'",
                "'#npc explode'", "'#npc hurt'", "'#npc drawtile'", "'#npc hide'", "'#npc remove'",
                "'#npc move'", "'#npc move to'", "'#npc move tp'", "'#npc move chase'",
                "'#npc move wander'", "'#npc move tank'", "'#npc move tp offset'", "'#npc anim play'",
                "'#npc anim frame'", "'#npc anim resume'", "'#npc anim throw'", "'#money spend'",
                "'#give item'", "'#tiles sprite'", "'#swap player item'", "'#quake'",
                "'#text'", "'#text option default'", "'#text quote'", "'#text event'",
                "'>'", "'#event open'", "'#event close'", "'#event wait'", "'#team join'",
                "'now'", "'#team leave'", "'#team hide'", "'#control change'", "'#menu'",
                "'#jmp'", "'#scroll'", "'#face'", "'#face back'", "'#face player'", "'#player face'",
                "'#player show'", "'#player hide'", "'#player hide all'", "'#player become'",
                "'#player tp'", "'quiet'", "'#player pull'", "'#player unpull'", "'#battle'",
                "'#wait time'", "'short'", "'normal'", "'long'", "'#wait event'", "'#flashscreen'",
                "'#conveyor'", "'stop'", "'forward'", "'reverse'", "'#changetankmap'",
                "'#recover'", "'#sleep'", "'#music'", "'#nextday'", "'#speaker'", "'#scene'",
                "'end'", "'#system'", "'#end'", "'#define'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, "DIRECTION", "LABEL",
                "LINE_COMMENT", "BLOCK_COMMENT", "TEXT", "STRING", "HEX", "BYTE", "BYTES",
                "NUMBER", "WS"
        };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "SpriteScript.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public SpriteScriptParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ProgramContext extends ParserRuleContext {
        public TypeStatementContext typeStatement() {
            return getRuleContext(TypeStatementContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(SpriteScriptParser.EOF, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public ProgramContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_program;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterProgram(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitProgram(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitProgram(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ProgramContext program() throws RecognitionException {
        ProgramContext _localctx = new ProgramContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_program);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(234);
                typeStatement();
                setState(238);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(235);
                            statement();
                        }
                    }
                    setState(240);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(241);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class StatementContext extends ParserRuleContext {
        public DefineStatementContext defineStatement() {
            return getRuleContext(DefineStatementContext.class, 0);
        }

        public CodeStatementContext codeStatement() {
            return getRuleContext(CodeStatementContext.class, 0);
        }

        public HenshinStatementContext henshinStatement() {
            return getRuleContext(HenshinStatementContext.class, 0);
        }

        public MedalStatementContext medalStatement() {
            return getRuleContext(MedalStatementContext.class, 0);
        }

        public TextPlainStatementContext textPlainStatement() {
            return getRuleContext(TextPlainStatementContext.class, 0);
        }

        public TextOptionStatementContext textOptionStatement() {
            return getRuleContext(TextOptionStatementContext.class, 0);
        }

        public TextQuoteStatementContext textQuoteStatement() {
            return getRuleContext(TextQuoteStatementContext.class, 0);
        }

        public TextEventStatementContext textEventStatement() {
            return getRuleContext(TextEventStatementContext.class, 0);
        }

        public IfEventStatementContext ifEventStatement() {
            return getRuleContext(IfEventStatementContext.class, 0);
        }

        public IfNotEventStatementContext ifNotEventStatement() {
            return getRuleContext(IfNotEventStatementContext.class, 0);
        }

        public IfTeamStatementContext ifTeamStatement() {
            return getRuleContext(IfTeamStatementContext.class, 0);
        }

        public IfNotTeamStatementContext ifNotTeamStatement() {
            return getRuleContext(IfNotTeamStatementContext.class, 0);
        }

        public IfTeammateDeadStatementContext ifTeammateDeadStatement() {
            return getRuleContext(IfTeammateDeadStatementContext.class, 0);
        }

        public IfNotTeammateDeadStatementContext ifNotTeammateDeadStatement() {
            return getRuleContext(IfNotTeammateDeadStatementContext.class, 0);
        }

        public IfRideTankStatementContext ifRideTankStatement() {
            return getRuleContext(IfRideTankStatementContext.class, 0);
        }

        public IfNotRideTankStatementContext ifNotRideTankStatement() {
            return getRuleContext(IfNotRideTankStatementContext.class, 0);
        }

        public IfTankRidingStatementContext ifTankRidingStatement() {
            return getRuleContext(IfTankRidingStatementContext.class, 0);
        }

        public IfNotTankRidingStatementContext ifNotTankRidingStatement() {
            return getRuleContext(IfNotTankRidingStatementContext.class, 0);
        }

        public IfTankHereStatementContext ifTankHereStatement() {
            return getRuleContext(IfTankHereStatementContext.class, 0);
        }

        public IfNotTankHereStatementContext ifNotTankHereStatement() {
            return getRuleContext(IfNotTankHereStatementContext.class, 0);
        }

        public IfHasOkTankStatementContext ifHasOkTankStatement() {
            return getRuleContext(IfHasOkTankStatementContext.class, 0);
        }

        public IfNotHasOkTankStatementContext ifNotHasOkTankStatement() {
            return getRuleContext(IfNotHasOkTankStatementContext.class, 0);
        }

        public IfHasItemStatementContext ifHasItemStatement() {
            return getRuleContext(IfHasItemStatementContext.class, 0);
        }

        public IfNotHasItemStatementContext ifNotHasItemStatement() {
            return getRuleContext(IfNotHasItemStatementContext.class, 0);
        }

        public IfFaceStatementContext ifFaceStatement() {
            return getRuleContext(IfFaceStatementContext.class, 0);
        }

        public IfNotFaceStatementContext ifNotFaceStatement() {
            return getRuleContext(IfNotFaceStatementContext.class, 0);
        }

        public IfLevelStatementContext ifLevelStatement() {
            return getRuleContext(IfLevelStatementContext.class, 0);
        }

        public IfNotLevelStatementContext ifNotLevelStatement() {
            return getRuleContext(IfNotLevelStatementContext.class, 0);
        }

        public IfXyStatementContext ifXyStatement() {
            return getRuleContext(IfXyStatementContext.class, 0);
        }

        public IfNotXyStatementContext ifNotXyStatement() {
            return getRuleContext(IfNotXyStatementContext.class, 0);
        }

        public IfAreaStatementContext ifAreaStatement() {
            return getRuleContext(IfAreaStatementContext.class, 0);
        }

        public IfNotAreaStatementContext ifNotAreaStatement() {
            return getRuleContext(IfNotAreaStatementContext.class, 0);
        }

        public IfMoneyStatementContext ifMoneyStatement() {
            return getRuleContext(IfMoneyStatementContext.class, 0);
        }

        public IfNotMoneyStatementContext ifNotMoneyStatement() {
            return getRuleContext(IfNotMoneyStatementContext.class, 0);
        }

        public IfTreasureStatementContext ifTreasureStatement() {
            return getRuleContext(IfTreasureStatementContext.class, 0);
        }

        public IfNotTreasureStatementContext ifNotTreasureStatement() {
            return getRuleContext(IfNotTreasureStatementContext.class, 0);
        }

        public IfHpStatementContext ifHpStatement() {
            return getRuleContext(IfHpStatementContext.class, 0);
        }

        public IfNotHpStatementContext ifNotHpStatement() {
            return getRuleContext(IfNotHpStatementContext.class, 0);
        }

        public IfOptionYesStatementContext ifOptionYesStatement() {
            return getRuleContext(IfOptionYesStatementContext.class, 0);
        }

        public IfNotOptionYesStatementContext ifNotOptionYesStatement() {
            return getRuleContext(IfNotOptionYesStatementContext.class, 0);
        }

        public IfDetectorStatementContext ifDetectorStatement() {
            return getRuleContext(IfDetectorStatementContext.class, 0);
        }

        public IfNotDetectorStatementContext ifNotDetectorStatement() {
            return getRuleContext(IfNotDetectorStatementContext.class, 0);
        }

        public IfDrankStatementContext ifDrankStatement() {
            return getRuleContext(IfDrankStatementContext.class, 0);
        }

        public IfNotDrankStatementContext ifNotDrankStatement() {
            return getRuleContext(IfNotDrankStatementContext.class, 0);
        }

        public DoLoopStatementContext doLoopStatement() {
            return getRuleContext(DoLoopStatementContext.class, 0);
        }

        public NpcModelStatementContext npcModelStatement() {
            return getRuleContext(NpcModelStatementContext.class, 0);
        }

        public NpcModelTileTypeStatementContext npcModelTileTypeStatement() {
            return getRuleContext(NpcModelTileTypeStatementContext.class, 0);
        }

        public EventOpenStatementContext eventOpenStatement() {
            return getRuleContext(EventOpenStatementContext.class, 0);
        }

        public EventCloseStatementContext eventCloseStatement() {
            return getRuleContext(EventCloseStatementContext.class, 0);
        }

        public EventWaitStatementContext eventWaitStatement() {
            return getRuleContext(EventWaitStatementContext.class, 0);
        }

        public TeamJoinStatementContext teamJoinStatement() {
            return getRuleContext(TeamJoinStatementContext.class, 0);
        }

        public TeamLeaveStatementContext teamLeaveStatement() {
            return getRuleContext(TeamLeaveStatementContext.class, 0);
        }

        public TeamHideStatementContext teamHideStatement() {
            return getRuleContext(TeamHideStatementContext.class, 0);
        }

        public ControlChangeStatementContext controlChangeStatement() {
            return getRuleContext(ControlChangeStatementContext.class, 0);
        }

        public MenuStatementContext menuStatement() {
            return getRuleContext(MenuStatementContext.class, 0);
        }

        public JmpStatementContext jmpStatement() {
            return getRuleContext(JmpStatementContext.class, 0);
        }

        public ScrollStatementContext scrollStatement() {
            return getRuleContext(ScrollStatementContext.class, 0);
        }

        public FaceStatementContext faceStatement() {
            return getRuleContext(FaceStatementContext.class, 0);
        }

        public FaceBackStatementContext faceBackStatement() {
            return getRuleContext(FaceBackStatementContext.class, 0);
        }

        public FacePlayerStatementContext facePlayerStatement() {
            return getRuleContext(FacePlayerStatementContext.class, 0);
        }

        public PlayerFaceStatementContext playerFaceStatement() {
            return getRuleContext(PlayerFaceStatementContext.class, 0);
        }

        public PlayerShowStatementContext playerShowStatement() {
            return getRuleContext(PlayerShowStatementContext.class, 0);
        }

        public PlayerShowCustomStatementContext playerShowCustomStatement() {
            return getRuleContext(PlayerShowCustomStatementContext.class, 0);
        }

        public PlayerHideStatementContext playerHideStatement() {
            return getRuleContext(PlayerHideStatementContext.class, 0);
        }

        public PlayerHideAllStatementContext playerHideAllStatement() {
            return getRuleContext(PlayerHideAllStatementContext.class, 0);
        }

        public PlayerBecomeStatementContext playerBecomeStatement() {
            return getRuleContext(PlayerBecomeStatementContext.class, 0);
        }

        public PlayerTpStatementContext playerTpStatement() {
            return getRuleContext(PlayerTpStatementContext.class, 0);
        }

        public PlayerPullStatementContext playerPullStatement() {
            return getRuleContext(PlayerPullStatementContext.class, 0);
        }

        public PlayerUnpullStatementContext playerUnpullStatement() {
            return getRuleContext(PlayerUnpullStatementContext.class, 0);
        }

        public BattleStatementContext battleStatement() {
            return getRuleContext(BattleStatementContext.class, 0);
        }

        public NpcActStatementContext npcActStatement() {
            return getRuleContext(NpcActStatementContext.class, 0);
        }

        public NpcAttrsStatementContext npcAttrsStatement() {
            return getRuleContext(NpcAttrsStatementContext.class, 0);
        }

        public NpcTankEnterStatementContext npcTankEnterStatement() {
            return getRuleContext(NpcTankEnterStatementContext.class, 0);
        }

        public NpcTankExitStatementContext npcTankExitStatement() {
            return getRuleContext(NpcTankExitStatementContext.class, 0);
        }

        public NpcPatrolStatementContext npcPatrolStatement() {
            return getRuleContext(NpcPatrolStatementContext.class, 0);
        }

        public NpcBecomeStatementContext npcBecomeStatement() {
            return getRuleContext(NpcBecomeStatementContext.class, 0);
        }

        public NpcOpenDoorStatementContext npcOpenDoorStatement() {
            return getRuleContext(NpcOpenDoorStatementContext.class, 0);
        }

        public NpcExplodeStatementContext npcExplodeStatement() {
            return getRuleContext(NpcExplodeStatementContext.class, 0);
        }

        public NpcHurtStatementContext npcHurtStatement() {
            return getRuleContext(NpcHurtStatementContext.class, 0);
        }

        public NpcDrawTileStatementContext npcDrawTileStatement() {
            return getRuleContext(NpcDrawTileStatementContext.class, 0);
        }

        public NpcHideStatementContext npcHideStatement() {
            return getRuleContext(NpcHideStatementContext.class, 0);
        }

        public NpcRemoveStatementContext npcRemoveStatement() {
            return getRuleContext(NpcRemoveStatementContext.class, 0);
        }

        public NpcMoveStatementContext npcMoveStatement() {
            return getRuleContext(NpcMoveStatementContext.class, 0);
        }

        public NpcMoveToStatementContext npcMoveToStatement() {
            return getRuleContext(NpcMoveToStatementContext.class, 0);
        }

        public NpcMoveTpStatementContext npcMoveTpStatement() {
            return getRuleContext(NpcMoveTpStatementContext.class, 0);
        }

        public NpcMoveChaseStatementContext npcMoveChaseStatement() {
            return getRuleContext(NpcMoveChaseStatementContext.class, 0);
        }

        public NpcMoveWanderStatementContext npcMoveWanderStatement() {
            return getRuleContext(NpcMoveWanderStatementContext.class, 0);
        }

        public NpcMoveTankStatementContext npcMoveTankStatement() {
            return getRuleContext(NpcMoveTankStatementContext.class, 0);
        }

        public NpcMoveTpOffsetStatementContext npcMoveTpOffsetStatement() {
            return getRuleContext(NpcMoveTpOffsetStatementContext.class, 0);
        }

        public NpcAnimPlayStatementContext npcAnimPlayStatement() {
            return getRuleContext(NpcAnimPlayStatementContext.class, 0);
        }

        public NpcAnimFrameStatementContext npcAnimFrameStatement() {
            return getRuleContext(NpcAnimFrameStatementContext.class, 0);
        }

        public NpcAnimResumeStatementContext npcAnimResumeStatement() {
            return getRuleContext(NpcAnimResumeStatementContext.class, 0);
        }

        public NpcAnimThrowStatementContext npcAnimThrowStatement() {
            return getRuleContext(NpcAnimThrowStatementContext.class, 0);
        }

        public MoneySpendStatementContext moneySpendStatement() {
            return getRuleContext(MoneySpendStatementContext.class, 0);
        }

        public GivePlayerItemStatementContext givePlayerItemStatement() {
            return getRuleContext(GivePlayerItemStatementContext.class, 0);
        }

        public TilesSpriteStatementContext tilesSpriteStatement() {
            return getRuleContext(TilesSpriteStatementContext.class, 0);
        }

        public SwapPlayerItemStatementContext swapPlayerItemStatement() {
            return getRuleContext(SwapPlayerItemStatementContext.class, 0);
        }

        public QuakeStatementContext quakeStatement() {
            return getRuleContext(QuakeStatementContext.class, 0);
        }

        public WaitTimeStatementContext waitTimeStatement() {
            return getRuleContext(WaitTimeStatementContext.class, 0);
        }

        public WaitEventStatementContext waitEventStatement() {
            return getRuleContext(WaitEventStatementContext.class, 0);
        }

        public FlashScreenStatementContext flashScreenStatement() {
            return getRuleContext(FlashScreenStatementContext.class, 0);
        }

        public ConveyorStatementContext conveyorStatement() {
            return getRuleContext(ConveyorStatementContext.class, 0);
        }

        public ChangeTankMapStatementContext changeTankMapStatement() {
            return getRuleContext(ChangeTankMapStatementContext.class, 0);
        }

        public RecoverStatementContext recoverStatement() {
            return getRuleContext(RecoverStatementContext.class, 0);
        }

        public SleepStatementContext sleepStatement() {
            return getRuleContext(SleepStatementContext.class, 0);
        }

        public MusicStatementContext musicStatement() {
            return getRuleContext(MusicStatementContext.class, 0);
        }

        public NextdayStatementContext nextdayStatement() {
            return getRuleContext(NextdayStatementContext.class, 0);
        }

        public SpeakerStatementContext speakerStatement() {
            return getRuleContext(SpeakerStatementContext.class, 0);
        }

        public SceneStatementContext sceneStatement() {
            return getRuleContext(SceneStatementContext.class, 0);
        }

        public SystemStatementContext systemStatement() {
            return getRuleContext(SystemStatementContext.class, 0);
        }

        public EndStatementContext endStatement() {
            return getRuleContext(EndStatementContext.class, 0);
        }

        public StatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final StatementContext statement() throws RecognitionException {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_statement);
        try {
            setState(354);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(243);
                    defineStatement();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(244);
                    codeStatement();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(245);
                    henshinStatement();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(246);
                    medalStatement();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(247);
                    textPlainStatement();
                }
                break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(248);
                    textOptionStatement();
                }
                break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(249);
                    textQuoteStatement();
                }
                break;
                case 8:
                    enterOuterAlt(_localctx, 8);
                {
                    setState(250);
                    textEventStatement();
                }
                break;
                case 9:
                    enterOuterAlt(_localctx, 9);
                {
                    setState(251);
                    ifEventStatement();
                }
                break;
                case 10:
                    enterOuterAlt(_localctx, 10);
                {
                    setState(252);
                    ifNotEventStatement();
                }
                break;
                case 11:
                    enterOuterAlt(_localctx, 11);
                {
                    setState(253);
                    ifTeamStatement();
                }
                break;
                case 12:
                    enterOuterAlt(_localctx, 12);
                {
                    setState(254);
                    ifNotTeamStatement();
                }
                break;
                case 13:
                    enterOuterAlt(_localctx, 13);
                {
                    setState(255);
                    ifTeammateDeadStatement();
                }
                break;
                case 14:
                    enterOuterAlt(_localctx, 14);
                {
                    setState(256);
                    ifNotTeammateDeadStatement();
                }
                break;
                case 15:
                    enterOuterAlt(_localctx, 15);
                {
                    setState(257);
                    ifRideTankStatement();
                }
                break;
                case 16:
                    enterOuterAlt(_localctx, 16);
                {
                    setState(258);
                    ifNotRideTankStatement();
                }
                break;
                case 17:
                    enterOuterAlt(_localctx, 17);
                {
                    setState(259);
                    ifTankRidingStatement();
                }
                break;
                case 18:
                    enterOuterAlt(_localctx, 18);
                {
                    setState(260);
                    ifNotTankRidingStatement();
                }
                break;
                case 19:
                    enterOuterAlt(_localctx, 19);
                {
                    setState(261);
                    ifTankHereStatement();
                }
                break;
                case 20:
                    enterOuterAlt(_localctx, 20);
                {
                    setState(262);
                    ifNotTankHereStatement();
                }
                break;
                case 21:
                    enterOuterAlt(_localctx, 21);
                {
                    setState(263);
                    ifHasOkTankStatement();
                }
                break;
                case 22:
                    enterOuterAlt(_localctx, 22);
                {
                    setState(264);
                    ifNotHasOkTankStatement();
                }
                break;
                case 23:
                    enterOuterAlt(_localctx, 23);
                {
                    setState(265);
                    ifHasItemStatement();
                }
                break;
                case 24:
                    enterOuterAlt(_localctx, 24);
                {
                    setState(266);
                    ifNotHasItemStatement();
                }
                break;
                case 25:
                    enterOuterAlt(_localctx, 25);
                {
                    setState(267);
                    ifFaceStatement();
                }
                break;
                case 26:
                    enterOuterAlt(_localctx, 26);
                {
                    setState(268);
                    ifNotFaceStatement();
                }
                break;
                case 27:
                    enterOuterAlt(_localctx, 27);
                {
                    setState(269);
                    ifLevelStatement();
                }
                break;
                case 28:
                    enterOuterAlt(_localctx, 28);
                {
                    setState(270);
                    ifNotLevelStatement();
                }
                break;
                case 29:
                    enterOuterAlt(_localctx, 29);
                {
                    setState(271);
                    ifXyStatement();
                }
                break;
                case 30:
                    enterOuterAlt(_localctx, 30);
                {
                    setState(272);
                    ifNotXyStatement();
                }
                break;
                case 31:
                    enterOuterAlt(_localctx, 31);
                {
                    setState(273);
                    ifAreaStatement();
                }
                break;
                case 32:
                    enterOuterAlt(_localctx, 32);
                {
                    setState(274);
                    ifNotAreaStatement();
                }
                break;
                case 33:
                    enterOuterAlt(_localctx, 33);
                {
                    setState(275);
                    ifMoneyStatement();
                }
                break;
                case 34:
                    enterOuterAlt(_localctx, 34);
                {
                    setState(276);
                    ifNotMoneyStatement();
                }
                break;
                case 35:
                    enterOuterAlt(_localctx, 35);
                {
                    setState(277);
                    ifTreasureStatement();
                }
                break;
                case 36:
                    enterOuterAlt(_localctx, 36);
                {
                    setState(278);
                    ifNotTreasureStatement();
                }
                break;
                case 37:
                    enterOuterAlt(_localctx, 37);
                {
                    setState(279);
                    ifHpStatement();
                }
                break;
                case 38:
                    enterOuterAlt(_localctx, 38);
                {
                    setState(280);
                    ifNotHpStatement();
                }
                break;
                case 39:
                    enterOuterAlt(_localctx, 39);
                {
                    setState(281);
                    ifOptionYesStatement();
                }
                break;
                case 40:
                    enterOuterAlt(_localctx, 40);
                {
                    setState(282);
                    ifNotOptionYesStatement();
                }
                break;
                case 41:
                    enterOuterAlt(_localctx, 41);
                {
                    setState(283);
                    ifDetectorStatement();
                }
                break;
                case 42:
                    enterOuterAlt(_localctx, 42);
                {
                    setState(284);
                    ifNotDetectorStatement();
                }
                break;
                case 43:
                    enterOuterAlt(_localctx, 43);
                {
                    setState(285);
                    ifDrankStatement();
                }
                break;
                case 44:
                    enterOuterAlt(_localctx, 44);
                {
                    setState(286);
                    ifNotDrankStatement();
                }
                break;
                case 45:
                    enterOuterAlt(_localctx, 45);
                {
                    setState(287);
                    doLoopStatement();
                }
                break;
                case 46:
                    enterOuterAlt(_localctx, 46);
                {
                    setState(288);
                    npcModelStatement();
                }
                break;
                case 47:
                    enterOuterAlt(_localctx, 47);
                {
                    setState(289);
                    npcModelTileTypeStatement();
                }
                break;
                case 48:
                    enterOuterAlt(_localctx, 48);
                {
                    setState(290);
                    eventOpenStatement();
                }
                break;
                case 49:
                    enterOuterAlt(_localctx, 49);
                {
                    setState(291);
                    eventCloseStatement();
                }
                break;
                case 50:
                    enterOuterAlt(_localctx, 50);
                {
                    setState(292);
                    eventWaitStatement();
                }
                break;
                case 51:
                    enterOuterAlt(_localctx, 51);
                {
                    setState(293);
                    teamJoinStatement();
                }
                break;
                case 52:
                    enterOuterAlt(_localctx, 52);
                {
                    setState(294);
                    teamLeaveStatement();
                }
                break;
                case 53:
                    enterOuterAlt(_localctx, 53);
                {
                    setState(295);
                    teamHideStatement();
                }
                break;
                case 54:
                    enterOuterAlt(_localctx, 54);
                {
                    setState(296);
                    controlChangeStatement();
                }
                break;
                case 55:
                    enterOuterAlt(_localctx, 55);
                {
                    setState(297);
                    menuStatement();
                }
                break;
                case 56:
                    enterOuterAlt(_localctx, 56);
                {
                    setState(298);
                    jmpStatement();
                }
                break;
                case 57:
                    enterOuterAlt(_localctx, 57);
                {
                    setState(299);
                    scrollStatement();
                }
                break;
                case 58:
                    enterOuterAlt(_localctx, 58);
                {
                    setState(300);
                    faceStatement();
                }
                break;
                case 59:
                    enterOuterAlt(_localctx, 59);
                {
                    setState(301);
                    faceBackStatement();
                }
                break;
                case 60:
                    enterOuterAlt(_localctx, 60);
                {
                    setState(302);
                    facePlayerStatement();
                }
                break;
                case 61:
                    enterOuterAlt(_localctx, 61);
                {
                    setState(303);
                    playerFaceStatement();
                }
                break;
                case 62:
                    enterOuterAlt(_localctx, 62);
                {
                    setState(304);
                    playerShowStatement();
                }
                break;
                case 63:
                    enterOuterAlt(_localctx, 63);
                {
                    setState(305);
                    playerShowCustomStatement();
                }
                break;
                case 64:
                    enterOuterAlt(_localctx, 64);
                {
                    setState(306);
                    playerHideStatement();
                }
                break;
                case 65:
                    enterOuterAlt(_localctx, 65);
                {
                    setState(307);
                    playerHideAllStatement();
                }
                break;
                case 66:
                    enterOuterAlt(_localctx, 66);
                {
                    setState(308);
                    playerBecomeStatement();
                }
                break;
                case 67:
                    enterOuterAlt(_localctx, 67);
                {
                    setState(309);
                    playerTpStatement();
                }
                break;
                case 68:
                    enterOuterAlt(_localctx, 68);
                {
                    setState(310);
                    playerPullStatement();
                }
                break;
                case 69:
                    enterOuterAlt(_localctx, 69);
                {
                    setState(311);
                    playerUnpullStatement();
                }
                break;
                case 70:
                    enterOuterAlt(_localctx, 70);
                {
                    setState(312);
                    battleStatement();
                }
                break;
                case 71:
                    enterOuterAlt(_localctx, 71);
                {
                    setState(313);
                    npcActStatement();
                }
                break;
                case 72:
                    enterOuterAlt(_localctx, 72);
                {
                    setState(314);
                    npcAttrsStatement();
                }
                break;
                case 73:
                    enterOuterAlt(_localctx, 73);
                {
                    setState(315);
                    npcTankEnterStatement();
                }
                break;
                case 74:
                    enterOuterAlt(_localctx, 74);
                {
                    setState(316);
                    npcTankExitStatement();
                }
                break;
                case 75:
                    enterOuterAlt(_localctx, 75);
                {
                    setState(317);
                    npcPatrolStatement();
                }
                break;
                case 76:
                    enterOuterAlt(_localctx, 76);
                {
                    setState(318);
                    npcBecomeStatement();
                }
                break;
                case 77:
                    enterOuterAlt(_localctx, 77);
                {
                    setState(319);
                    npcOpenDoorStatement();
                }
                break;
                case 78:
                    enterOuterAlt(_localctx, 78);
                {
                    setState(320);
                    npcExplodeStatement();
                }
                break;
                case 79:
                    enterOuterAlt(_localctx, 79);
                {
                    setState(321);
                    npcHurtStatement();
                }
                break;
                case 80:
                    enterOuterAlt(_localctx, 80);
                {
                    setState(322);
                    npcDrawTileStatement();
                }
                break;
                case 81:
                    enterOuterAlt(_localctx, 81);
                {
                    setState(323);
                    npcHideStatement();
                }
                break;
                case 82:
                    enterOuterAlt(_localctx, 82);
                {
                    setState(324);
                    npcRemoveStatement();
                }
                break;
                case 83:
                    enterOuterAlt(_localctx, 83);
                {
                    setState(325);
                    npcMoveStatement();
                }
                break;
                case 84:
                    enterOuterAlt(_localctx, 84);
                {
                    setState(326);
                    npcMoveToStatement();
                }
                break;
                case 85:
                    enterOuterAlt(_localctx, 85);
                {
                    setState(327);
                    npcMoveTpStatement();
                }
                break;
                case 86:
                    enterOuterAlt(_localctx, 86);
                {
                    setState(328);
                    npcMoveChaseStatement();
                }
                break;
                case 87:
                    enterOuterAlt(_localctx, 87);
                {
                    setState(329);
                    npcMoveWanderStatement();
                }
                break;
                case 88:
                    enterOuterAlt(_localctx, 88);
                {
                    setState(330);
                    npcMoveTankStatement();
                }
                break;
                case 89:
                    enterOuterAlt(_localctx, 89);
                {
                    setState(331);
                    npcMoveTpOffsetStatement();
                }
                break;
                case 90:
                    enterOuterAlt(_localctx, 90);
                {
                    setState(332);
                    npcAnimPlayStatement();
                }
                break;
                case 91:
                    enterOuterAlt(_localctx, 91);
                {
                    setState(333);
                    npcAnimFrameStatement();
                }
                break;
                case 92:
                    enterOuterAlt(_localctx, 92);
                {
                    setState(334);
                    npcAnimResumeStatement();
                }
                break;
                case 93:
                    enterOuterAlt(_localctx, 93);
                {
                    setState(335);
                    npcAnimThrowStatement();
                }
                break;
                case 94:
                    enterOuterAlt(_localctx, 94);
                {
                    setState(336);
                    moneySpendStatement();
                }
                break;
                case 95:
                    enterOuterAlt(_localctx, 95);
                {
                    setState(337);
                    givePlayerItemStatement();
                }
                break;
                case 96:
                    enterOuterAlt(_localctx, 96);
                {
                    setState(338);
                    tilesSpriteStatement();
                }
                break;
                case 97:
                    enterOuterAlt(_localctx, 97);
                {
                    setState(339);
                    swapPlayerItemStatement();
                }
                break;
                case 98:
                    enterOuterAlt(_localctx, 98);
                {
                    setState(340);
                    quakeStatement();
                }
                break;
                case 99:
                    enterOuterAlt(_localctx, 99);
                {
                    setState(341);
                    waitTimeStatement();
                }
                break;
                case 100:
                    enterOuterAlt(_localctx, 100);
                {
                    setState(342);
                    waitEventStatement();
                }
                break;
                case 101:
                    enterOuterAlt(_localctx, 101);
                {
                    setState(343);
                    flashScreenStatement();
                }
                break;
                case 102:
                    enterOuterAlt(_localctx, 102);
                {
                    setState(344);
                    conveyorStatement();
                }
                break;
                case 103:
                    enterOuterAlt(_localctx, 103);
                {
                    setState(345);
                    changeTankMapStatement();
                }
                break;
                case 104:
                    enterOuterAlt(_localctx, 104);
                {
                    setState(346);
                    recoverStatement();
                }
                break;
                case 105:
                    enterOuterAlt(_localctx, 105);
                {
                    setState(347);
                    sleepStatement();
                }
                break;
                case 106:
                    enterOuterAlt(_localctx, 106);
                {
                    setState(348);
                    musicStatement();
                }
                break;
                case 107:
                    enterOuterAlt(_localctx, 107);
                {
                    setState(349);
                    nextdayStatement();
                }
                break;
                case 108:
                    enterOuterAlt(_localctx, 108);
                {
                    setState(350);
                    speakerStatement();
                }
                break;
                case 109:
                    enterOuterAlt(_localctx, 109);
                {
                    setState(351);
                    sceneStatement();
                }
                break;
                case 110:
                    enterOuterAlt(_localctx, 110);
                {
                    setState(352);
                    systemStatement();
                }
                break;
                case 111:
                    enterOuterAlt(_localctx, 111);
                {
                    setState(353);
                    endStatement();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TypeStatementContext extends ParserRuleContext {
        public TypeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_typeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterTypeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitTypeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTypeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TypeStatementContext typeStatement() throws RecognitionException {
        TypeStatementContext _localctx = new TypeStatementContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_typeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(356);
                match(T__0);
                setState(357);
                _la = _input.LA(1);
                if (!(_la == T__1 || _la == T__2)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class HenshinStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public HenshinStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_henshinStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterHenshinStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitHenshinStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitHenshinStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final HenshinStatementContext henshinStatement() throws RecognitionException {
        HenshinStatementContext _localctx = new HenshinStatementContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_henshinStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(360);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(359);
                        match(LABEL);
                    }
                }

                setState(362);
                match(T__3);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MedalStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public MedalStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_medalStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterMedalStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitMedalStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitMedalStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final MedalStatementContext medalStatement() throws RecognitionException {
        MedalStatementContext _localctx = new MedalStatementContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_medalStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(365);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(364);
                        match(LABEL);
                    }
                }

                setState(367);
                match(T__4);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class CodeStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public CodeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_codeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterCodeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitCodeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitCodeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CodeStatementContext codeStatement() throws RecognitionException {
        CodeStatementContext _localctx = new CodeStatementContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_codeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(370);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(369);
                        match(LABEL);
                    }
                }

                setState(372);
                match(T__5);
                setState(373);
                match(BYTE);
                setState(377);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == BYTE) {
                    {
                        {
                            setState(374);
                            match(BYTE);
                        }
                    }
                    setState(379);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfEventStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfEventStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifEventStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfEventStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfEventStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfEventStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfEventStatementContext ifEventStatement() throws RecognitionException {
        IfEventStatementContext _localctx = new IfEventStatementContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_ifEventStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(381);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(380);
                        match(LABEL);
                    }
                }

                setState(383);
                match(T__6);
                setState(384);
                match(HEX);
                setState(388);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(385);
                            statement();
                        }
                    }
                    setState(390);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(391);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotEventStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotEventStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotEventStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotEventStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotEventStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotEventStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotEventStatementContext ifNotEventStatement() throws RecognitionException {
        IfNotEventStatementContext _localctx = new IfNotEventStatementContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_ifNotEventStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(394);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(393);
                        match(LABEL);
                    }
                }

                setState(396);
                match(T__7);
                setState(397);
                match(HEX);
                setState(398);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfTeamStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfTeamStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifTeamStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfTeamStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfTeamStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfTeamStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfTeamStatementContext ifTeamStatement() throws RecognitionException {
        IfTeamStatementContext _localctx = new IfTeamStatementContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_ifTeamStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(401);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(400);
                        match(LABEL);
                    }
                }

                setState(403);
                match(T__8);
                setState(404);
                match(HEX);
                setState(408);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(405);
                            statement();
                        }
                    }
                    setState(410);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(411);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotTeamStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotTeamStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotTeamStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotTeamStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotTeamStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotTeamStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotTeamStatementContext ifNotTeamStatement() throws RecognitionException {
        IfNotTeamStatementContext _localctx = new IfNotTeamStatementContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_ifNotTeamStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(414);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(413);
                        match(LABEL);
                    }
                }

                setState(416);
                match(T__9);
                setState(417);
                match(HEX);
                setState(418);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfTeammateDeadStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfTeammateDeadStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifTeammateDeadStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfTeammateDeadStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfTeammateDeadStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfTeammateDeadStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfTeammateDeadStatementContext ifTeammateDeadStatement() throws RecognitionException {
        IfTeammateDeadStatementContext _localctx = new IfTeammateDeadStatementContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_ifTeammateDeadStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(421);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(420);
                        match(LABEL);
                    }
                }

                setState(423);
                match(T__10);
                setState(424);
                match(HEX);
                setState(428);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(425);
                            statement();
                        }
                    }
                    setState(430);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(431);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotTeammateDeadStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotTeammateDeadStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotTeammateDeadStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotTeammateDeadStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotTeammateDeadStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotTeammateDeadStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotTeammateDeadStatementContext ifNotTeammateDeadStatement() throws RecognitionException {
        IfNotTeammateDeadStatementContext _localctx = new IfNotTeammateDeadStatementContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_ifNotTeammateDeadStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(434);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(433);
                        match(LABEL);
                    }
                }

                setState(436);
                match(T__11);
                setState(437);
                match(HEX);
                setState(438);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfRideTankStatementContext extends ParserRuleContext {
        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfRideTankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifRideTankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfRideTankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfRideTankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfRideTankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfRideTankStatementContext ifRideTankStatement() throws RecognitionException {
        IfRideTankStatementContext _localctx = new IfRideTankStatementContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_ifRideTankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(441);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(440);
                        match(LABEL);
                    }
                }

                setState(443);
                match(T__12);
                setState(447);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(444);
                            statement();
                        }
                    }
                    setState(449);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(450);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotRideTankStatementContext extends ParserRuleContext {
        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotRideTankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotRideTankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotRideTankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotRideTankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotRideTankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotRideTankStatementContext ifNotRideTankStatement() throws RecognitionException {
        IfNotRideTankStatementContext _localctx = new IfNotRideTankStatementContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_ifNotRideTankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(453);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(452);
                        match(LABEL);
                    }
                }

                setState(455);
                match(T__13);
                setState(456);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfTankRidingStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfTankRidingStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifTankRidingStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfTankRidingStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfTankRidingStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfTankRidingStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfTankRidingStatementContext ifTankRidingStatement() throws RecognitionException {
        IfTankRidingStatementContext _localctx = new IfTankRidingStatementContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_ifTankRidingStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(459);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(458);
                        match(LABEL);
                    }
                }

                setState(461);
                match(T__14);
                setState(462);
                match(HEX);
                setState(466);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(463);
                            statement();
                        }
                    }
                    setState(468);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(469);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotTankRidingStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotTankRidingStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotTankRidingStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotTankRidingStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotTankRidingStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotTankRidingStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotTankRidingStatementContext ifNotTankRidingStatement() throws RecognitionException {
        IfNotTankRidingStatementContext _localctx = new IfNotTankRidingStatementContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_ifNotTankRidingStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(472);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(471);
                        match(LABEL);
                    }
                }

                setState(474);
                match(T__15);
                setState(475);
                match(HEX);
                setState(476);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfTankHereStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfTankHereStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifTankHereStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfTankHereStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfTankHereStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfTankHereStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfTankHereStatementContext ifTankHereStatement() throws RecognitionException {
        IfTankHereStatementContext _localctx = new IfTankHereStatementContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_ifTankHereStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(479);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(478);
                        match(LABEL);
                    }
                }

                setState(481);
                match(T__16);
                setState(482);
                match(HEX);
                setState(486);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(483);
                            statement();
                        }
                    }
                    setState(488);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(489);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotTankHereStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotTankHereStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotTankHereStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotTankHereStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotTankHereStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotTankHereStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotTankHereStatementContext ifNotTankHereStatement() throws RecognitionException {
        IfNotTankHereStatementContext _localctx = new IfNotTankHereStatementContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_ifNotTankHereStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(492);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(491);
                        match(LABEL);
                    }
                }

                setState(494);
                match(T__17);
                setState(495);
                match(HEX);
                setState(496);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfHasOkTankStatementContext extends ParserRuleContext {
        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfHasOkTankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifHasOkTankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfHasOkTankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfHasOkTankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfHasOkTankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfHasOkTankStatementContext ifHasOkTankStatement() throws RecognitionException {
        IfHasOkTankStatementContext _localctx = new IfHasOkTankStatementContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_ifHasOkTankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(499);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(498);
                        match(LABEL);
                    }
                }

                setState(501);
                match(T__18);
                setState(505);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(502);
                            statement();
                        }
                    }
                    setState(507);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(508);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotHasOkTankStatementContext extends ParserRuleContext {
        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotHasOkTankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotHasOkTankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotHasOkTankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotHasOkTankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotHasOkTankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotHasOkTankStatementContext ifNotHasOkTankStatement() throws RecognitionException {
        IfNotHasOkTankStatementContext _localctx = new IfNotHasOkTankStatementContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_ifNotHasOkTankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(511);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(510);
                        match(LABEL);
                    }
                }

                setState(513);
                match(T__19);
                setState(514);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfHasItemStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfHasItemStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifHasItemStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfHasItemStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfHasItemStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfHasItemStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfHasItemStatementContext ifHasItemStatement() throws RecognitionException {
        IfHasItemStatementContext _localctx = new IfHasItemStatementContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_ifHasItemStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(517);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(516);
                        match(LABEL);
                    }
                }

                setState(519);
                match(T__20);
                setState(520);
                match(HEX);
                setState(524);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(521);
                            statement();
                        }
                    }
                    setState(526);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(527);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotHasItemStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotHasItemStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotHasItemStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotHasItemStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotHasItemStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotHasItemStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotHasItemStatementContext ifNotHasItemStatement() throws RecognitionException {
        IfNotHasItemStatementContext _localctx = new IfNotHasItemStatementContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_ifNotHasItemStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(530);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(529);
                        match(LABEL);
                    }
                }

                setState(532);
                match(T__21);
                setState(533);
                match(HEX);
                setState(534);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfFaceStatementContext extends ParserRuleContext {
        public TerminalNode DIRECTION() {
            return getToken(SpriteScriptParser.DIRECTION, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfFaceStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifFaceStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfFaceStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfFaceStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfFaceStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfFaceStatementContext ifFaceStatement() throws RecognitionException {
        IfFaceStatementContext _localctx = new IfFaceStatementContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_ifFaceStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(537);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(536);
                        match(LABEL);
                    }
                }

                setState(539);
                match(T__22);
                setState(540);
                match(DIRECTION);
                setState(544);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(541);
                            statement();
                        }
                    }
                    setState(546);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(547);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotFaceStatementContext extends ParserRuleContext {
        public TerminalNode DIRECTION() {
            return getToken(SpriteScriptParser.DIRECTION, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotFaceStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotFaceStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotFaceStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotFaceStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotFaceStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotFaceStatementContext ifNotFaceStatement() throws RecognitionException {
        IfNotFaceStatementContext _localctx = new IfNotFaceStatementContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_ifNotFaceStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(550);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(549);
                        match(LABEL);
                    }
                }

                setState(552);
                match(T__23);
                setState(553);
                match(DIRECTION);
                setState(554);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfLevelStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfLevelStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifLevelStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfLevelStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfLevelStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfLevelStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfLevelStatementContext ifLevelStatement() throws RecognitionException {
        IfLevelStatementContext _localctx = new IfLevelStatementContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_ifLevelStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(557);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(556);
                        match(LABEL);
                    }
                }

                setState(559);
                match(T__24);
                setState(560);
                match(NUMBER);
                setState(564);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(561);
                            statement();
                        }
                    }
                    setState(566);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(567);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotLevelStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotLevelStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotLevelStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotLevelStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotLevelStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotLevelStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotLevelStatementContext ifNotLevelStatement() throws RecognitionException {
        IfNotLevelStatementContext _localctx = new IfNotLevelStatementContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_ifNotLevelStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(570);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(569);
                        match(LABEL);
                    }
                }

                setState(572);
                match(T__25);
                setState(573);
                match(NUMBER);
                setState(574);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfXyStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfXyStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifXyStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfXyStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfXyStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfXyStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfXyStatementContext ifXyStatement() throws RecognitionException {
        IfXyStatementContext _localctx = new IfXyStatementContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_ifXyStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(577);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(576);
                        match(LABEL);
                    }
                }

                setState(579);
                match(T__26);
                setState(580);
                match(BYTE);
                setState(581);
                match(BYTE);
                setState(585);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(582);
                            statement();
                        }
                    }
                    setState(587);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(588);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotXyStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotXyStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotXyStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfNotXyStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfNotXyStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotXyStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotXyStatementContext ifNotXyStatement() throws RecognitionException {
        IfNotXyStatementContext _localctx = new IfNotXyStatementContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_ifNotXyStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(591);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(590);
                        match(LABEL);
                    }
                }

                setState(593);
                match(T__27);
                setState(594);
                match(BYTE);
                setState(595);
                match(BYTE);
                setState(596);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfAreaStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfAreaStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifAreaStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfAreaStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfAreaStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfAreaStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfAreaStatementContext ifAreaStatement() throws RecognitionException {
        IfAreaStatementContext _localctx = new IfAreaStatementContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_ifAreaStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(599);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(598);
                        match(LABEL);
                    }
                }

                setState(601);
                match(T__28);
                setState(602);
                match(BYTE);
                setState(603);
                match(BYTE);
                setState(604);
                match(BYTE);
                setState(605);
                match(BYTE);
                setState(609);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(606);
                            statement();
                        }
                    }
                    setState(611);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(612);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotAreaStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotAreaStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotAreaStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotAreaStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotAreaStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotAreaStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotAreaStatementContext ifNotAreaStatement() throws RecognitionException {
        IfNotAreaStatementContext _localctx = new IfNotAreaStatementContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_ifNotAreaStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(615);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(614);
                        match(LABEL);
                    }
                }

                setState(617);
                match(T__29);
                setState(618);
                match(BYTE);
                setState(619);
                match(BYTE);
                setState(620);
                match(BYTE);
                setState(621);
                match(BYTE);
                setState(622);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfMoneyStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfMoneyStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifMoneyStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfMoneyStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfMoneyStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfMoneyStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfMoneyStatementContext ifMoneyStatement() throws RecognitionException {
        IfMoneyStatementContext _localctx = new IfMoneyStatementContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_ifMoneyStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(625);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(624);
                        match(LABEL);
                    }
                }

                setState(627);
                match(T__30);
                setState(628);
                match(NUMBER);
                setState(632);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(629);
                            statement();
                        }
                    }
                    setState(634);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(635);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotMoneyStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotMoneyStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotMoneyStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotMoneyStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotMoneyStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotMoneyStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotMoneyStatementContext ifNotMoneyStatement() throws RecognitionException {
        IfNotMoneyStatementContext _localctx = new IfNotMoneyStatementContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_ifNotMoneyStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(638);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(637);
                        match(LABEL);
                    }
                }

                setState(640);
                match(T__31);
                setState(641);
                match(NUMBER);
                setState(642);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfTreasureStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfTreasureStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifTreasureStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfTreasureStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfTreasureStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfTreasureStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfTreasureStatementContext ifTreasureStatement() throws RecognitionException {
        IfTreasureStatementContext _localctx = new IfTreasureStatementContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_ifTreasureStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(645);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(644);
                        match(LABEL);
                    }
                }

                setState(647);
                match(T__32);
                setState(648);
                match(BYTE);
                setState(652);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(649);
                            statement();
                        }
                    }
                    setState(654);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(655);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotTreasureStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotTreasureStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotTreasureStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotTreasureStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotTreasureStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotTreasureStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotTreasureStatementContext ifNotTreasureStatement() throws RecognitionException {
        IfNotTreasureStatementContext _localctx = new IfNotTreasureStatementContext(_ctx, getState());
        enterRule(_localctx, 66, RULE_ifNotTreasureStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(658);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(657);
                        match(LABEL);
                    }
                }

                setState(660);
                match(T__33);
                setState(661);
                match(BYTE);
                setState(662);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfHpStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfHpStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifHpStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfHpStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfHpStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfHpStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfHpStatementContext ifHpStatement() throws RecognitionException {
        IfHpStatementContext _localctx = new IfHpStatementContext(_ctx, getState());
        enterRule(_localctx, 68, RULE_ifHpStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(665);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(664);
                        match(LABEL);
                    }
                }

                setState(667);
                match(T__34);
                setState(668);
                match(NUMBER);
                setState(672);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(669);
                            statement();
                        }
                    }
                    setState(674);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(675);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotHpStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotHpStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotHpStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfNotHpStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfNotHpStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotHpStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotHpStatementContext ifNotHpStatement() throws RecognitionException {
        IfNotHpStatementContext _localctx = new IfNotHpStatementContext(_ctx, getState());
        enterRule(_localctx, 70, RULE_ifNotHpStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(678);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(677);
                        match(LABEL);
                    }
                }

                setState(680);
                match(T__35);
                setState(681);
                match(NUMBER);
                setState(682);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfOptionYesStatementContext extends ParserRuleContext {
        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfOptionYesStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifOptionYesStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfOptionYesStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfOptionYesStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfOptionYesStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfOptionYesStatementContext ifOptionYesStatement() throws RecognitionException {
        IfOptionYesStatementContext _localctx = new IfOptionYesStatementContext(_ctx, getState());
        enterRule(_localctx, 72, RULE_ifOptionYesStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(685);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(684);
                        match(LABEL);
                    }
                }

                setState(687);
                match(T__36);
                setState(691);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(688);
                            statement();
                        }
                    }
                    setState(693);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(694);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotOptionYesStatementContext extends ParserRuleContext {
        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotOptionYesStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotOptionYesStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotOptionYesStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotOptionYesStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotOptionYesStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotOptionYesStatementContext ifNotOptionYesStatement() throws RecognitionException {
        IfNotOptionYesStatementContext _localctx = new IfNotOptionYesStatementContext(_ctx, getState());
        enterRule(_localctx, 74, RULE_ifNotOptionYesStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(697);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(696);
                        match(LABEL);
                    }
                }

                setState(699);
                match(T__37);
                setState(700);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfDetectorStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfDetectorStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifDetectorStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfDetectorStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfDetectorStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfDetectorStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfDetectorStatementContext ifDetectorStatement() throws RecognitionException {
        IfDetectorStatementContext _localctx = new IfDetectorStatementContext(_ctx, getState());
        enterRule(_localctx, 76, RULE_ifDetectorStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(703);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(702);
                        match(LABEL);
                    }
                }

                setState(705);
                match(T__38);
                setState(706);
                match(NUMBER);
                setState(710);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(707);
                            statement();
                        }
                    }
                    setState(712);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(713);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotDetectorStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotDetectorStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotDetectorStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotDetectorStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotDetectorStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotDetectorStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotDetectorStatementContext ifNotDetectorStatement() throws RecognitionException {
        IfNotDetectorStatementContext _localctx = new IfNotDetectorStatementContext(_ctx, getState());
        enterRule(_localctx, 78, RULE_ifNotDetectorStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(716);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(715);
                        match(LABEL);
                    }
                }

                setState(718);
                match(T__39);
                setState(719);
                match(NUMBER);
                setState(720);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfDrankStatementContext extends ParserRuleContext {
        public EndifStatementContext endifStatement() {
            return getRuleContext(EndifStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public IfDrankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifDrankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterIfDrankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitIfDrankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfDrankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfDrankStatementContext ifDrankStatement() throws RecognitionException {
        IfDrankStatementContext _localctx = new IfDrankStatementContext(_ctx, getState());
        enterRule(_localctx, 80, RULE_ifDrankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(723);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(722);
                        match(LABEL);
                    }
                }

                setState(725);
                match(T__40);
                setState(729);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(726);
                            statement();
                        }
                    }
                    setState(731);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(732);
                endifStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfNotDrankStatementContext extends ParserRuleContext {
        public LabelTargetStatementContext labelTargetStatement() {
            return getRuleContext(LabelTargetStatementContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public IfNotDrankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifNotDrankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterIfNotDrankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitIfNotDrankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitIfNotDrankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfNotDrankStatementContext ifNotDrankStatement() throws RecognitionException {
        IfNotDrankStatementContext _localctx = new IfNotDrankStatementContext(_ctx, getState());
        enterRule(_localctx, 82, RULE_ifNotDrankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(735);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(734);
                        match(LABEL);
                    }
                }

                setState(737);
                match(T__41);
                setState(738);
                labelTargetStatement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EndifStatementContext extends ParserRuleContext {
        public EndifStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_endifStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterEndifStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitEndifStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitEndifStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EndifStatementContext endifStatement() throws RecognitionException {
        EndifStatementContext _localctx = new EndifStatementContext(_ctx, getState());
        enterRule(_localctx, 84, RULE_endifStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(740);
                _la = _input.LA(1);
                if (!(_la == T__42 || _la == T__43)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LabelTargetStatementContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(SpriteScriptParser.STRING, 0);
        }

        public LabelTargetStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_labelTargetStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterLabelTargetStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitLabelTargetStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitLabelTargetStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final LabelTargetStatementContext labelTargetStatement() throws RecognitionException {
        LabelTargetStatementContext _localctx = new LabelTargetStatementContext(_ctx, getState());
        enterRule(_localctx, 86, RULE_labelTargetStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(742);
                match(STRING);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DoLoopStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public DoLoopStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_doLoopStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterDoLoopStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitDoLoopStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitDoLoopStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DoLoopStatementContext doLoopStatement() throws RecognitionException {
        DoLoopStatementContext _localctx = new DoLoopStatementContext(_ctx, getState());
        enterRule(_localctx, 88, RULE_doLoopStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(745);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(744);
                        match(LABEL);
                    }
                }

                setState(747);
                match(T__44);
                setState(751);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((((_la - 4)) & ~0x3f) == 0 && ((1L << (_la - 4)) & -58823872086017L) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 6772413215263391743L) != 0)) {
                    {
                        {
                            setState(748);
                            statement();
                        }
                    }
                    setState(753);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(754);
                match(T__45);
                setState(755);
                match(NUMBER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcModelStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcModelStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcModelStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcModelStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNpcModelStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcModelStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcModelStatementContext npcModelStatement() throws RecognitionException {
        NpcModelStatementContext _localctx = new NpcModelStatementContext(_ctx, getState());
        enterRule(_localctx, 90, RULE_npcModelStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(758);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(757);
                        match(LABEL);
                    }
                }

                setState(760);
                match(T__46);
                setState(763);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T__47:
                    case T__48: {
                        setState(761);
                        _la = _input.LA(1);
                        if (!(_la == T__47 || _la == T__48)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                    break;
                    case BYTE: {
                        setState(762);
                        match(BYTE);
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcModelTileTypeStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcModelTileTypeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcModelTileTypeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcModelTileTypeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcModelTileTypeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcModelTileTypeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcModelTileTypeStatementContext npcModelTileTypeStatement() throws RecognitionException {
        NpcModelTileTypeStatementContext _localctx = new NpcModelTileTypeStatementContext(_ctx, getState());
        enterRule(_localctx, 92, RULE_npcModelTileTypeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(766);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(765);
                        match(LABEL);
                    }
                }

                setState(768);
                match(T__49);
                setState(769);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcActStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcActStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcActStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterNpcActStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNpcActStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcActStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcActStatementContext npcActStatement() throws RecognitionException {
        NpcActStatementContext _localctx = new NpcActStatementContext(_ctx, getState());
        enterRule(_localctx, 94, RULE_npcActStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(772);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(771);
                        match(LABEL);
                    }
                }

                setState(774);
                match(T__50);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcAttrsStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcAttrsStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcAttrsStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcAttrsStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNpcAttrsStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcAttrsStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcAttrsStatementContext npcAttrsStatement() throws RecognitionException {
        NpcAttrsStatementContext _localctx = new NpcAttrsStatementContext(_ctx, getState());
        enterRule(_localctx, 96, RULE_npcAttrsStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(777);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(776);
                        match(LABEL);
                    }
                }

                setState(779);
                match(T__51);
                setState(780);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcTankEnterStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcTankEnterStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcTankEnterStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcTankEnterStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcTankEnterStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcTankEnterStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcTankEnterStatementContext npcTankEnterStatement() throws RecognitionException {
        NpcTankEnterStatementContext _localctx = new NpcTankEnterStatementContext(_ctx, getState());
        enterRule(_localctx, 98, RULE_npcTankEnterStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(783);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(782);
                        match(LABEL);
                    }
                }

                setState(785);
                match(T__52);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcTankExitStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcTankExitStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcTankExitStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcTankExitStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcTankExitStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcTankExitStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcTankExitStatementContext npcTankExitStatement() throws RecognitionException {
        NpcTankExitStatementContext _localctx = new NpcTankExitStatementContext(_ctx, getState());
        enterRule(_localctx, 100, RULE_npcTankExitStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(788);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(787);
                        match(LABEL);
                    }
                }

                setState(790);
                match(T__53);
                setState(791);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcPatrolStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcPatrolStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcPatrolStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcPatrolStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcPatrolStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcPatrolStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcPatrolStatementContext npcPatrolStatement() throws RecognitionException {
        NpcPatrolStatementContext _localctx = new NpcPatrolStatementContext(_ctx, getState());
        enterRule(_localctx, 102, RULE_npcPatrolStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(794);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(793);
                        match(LABEL);
                    }
                }

                setState(796);
                match(T__54);
                setState(797);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcBecomeStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcBecomeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcBecomeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcBecomeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcBecomeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcBecomeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcBecomeStatementContext npcBecomeStatement() throws RecognitionException {
        NpcBecomeStatementContext _localctx = new NpcBecomeStatementContext(_ctx, getState());
        enterRule(_localctx, 104, RULE_npcBecomeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(800);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(799);
                        match(LABEL);
                    }
                }

                setState(802);
                match(T__55);
                setState(803);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcOpenDoorStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcOpenDoorStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcOpenDoorStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcOpenDoorStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcOpenDoorStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcOpenDoorStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcOpenDoorStatementContext npcOpenDoorStatement() throws RecognitionException {
        NpcOpenDoorStatementContext _localctx = new NpcOpenDoorStatementContext(_ctx, getState());
        enterRule(_localctx, 106, RULE_npcOpenDoorStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(806);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(805);
                        match(LABEL);
                    }
                }

                setState(808);
                match(T__56);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcExplodeStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcExplodeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcExplodeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcExplodeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcExplodeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcExplodeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcExplodeStatementContext npcExplodeStatement() throws RecognitionException {
        NpcExplodeStatementContext _localctx = new NpcExplodeStatementContext(_ctx, getState());
        enterRule(_localctx, 108, RULE_npcExplodeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(811);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(810);
                        match(LABEL);
                    }
                }

                setState(813);
                match(T__57);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcHurtStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcHurtStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcHurtStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterNpcHurtStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNpcHurtStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcHurtStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcHurtStatementContext npcHurtStatement() throws RecognitionException {
        NpcHurtStatementContext _localctx = new NpcHurtStatementContext(_ctx, getState());
        enterRule(_localctx, 110, RULE_npcHurtStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(816);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(815);
                        match(LABEL);
                    }
                }

                setState(818);
                match(T__58);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcDrawTileStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcDrawTileStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcDrawTileStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcDrawTileStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcDrawTileStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcDrawTileStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcDrawTileStatementContext npcDrawTileStatement() throws RecognitionException {
        NpcDrawTileStatementContext _localctx = new NpcDrawTileStatementContext(_ctx, getState());
        enterRule(_localctx, 112, RULE_npcDrawTileStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(821);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(820);
                        match(LABEL);
                    }
                }

                setState(823);
                match(T__59);
                setState(824);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcHideStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcHideStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcHideStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterNpcHideStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNpcHideStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcHideStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcHideStatementContext npcHideStatement() throws RecognitionException {
        NpcHideStatementContext _localctx = new NpcHideStatementContext(_ctx, getState());
        enterRule(_localctx, 114, RULE_npcHideStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(827);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(826);
                        match(LABEL);
                    }
                }

                setState(829);
                match(T__60);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcRemoveStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcRemoveStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcRemoveStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcRemoveStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcRemoveStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcRemoveStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcRemoveStatementContext npcRemoveStatement() throws RecognitionException {
        NpcRemoveStatementContext _localctx = new NpcRemoveStatementContext(_ctx, getState());
        enterRule(_localctx, 116, RULE_npcRemoveStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(832);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(831);
                        match(LABEL);
                    }
                }

                setState(834);
                match(T__61);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveStatementContext extends ParserRuleContext {
        public TerminalNode DIRECTION() {
            return getToken(SpriteScriptParser.DIRECTION, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public NpcMoveStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterNpcMoveStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNpcMoveStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveStatementContext npcMoveStatement() throws RecognitionException {
        NpcMoveStatementContext _localctx = new NpcMoveStatementContext(_ctx, getState());
        enterRule(_localctx, 118, RULE_npcMoveStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(837);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(836);
                        match(LABEL);
                    }
                }

                setState(839);
                match(T__62);
                setState(840);
                match(DIRECTION);
                setState(842);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == NUMBER) {
                    {
                        setState(841);
                        match(NUMBER);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveToStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcMoveToStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveToStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcMoveToStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcMoveToStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveToStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveToStatementContext npcMoveToStatement() throws RecognitionException {
        NpcMoveToStatementContext _localctx = new NpcMoveToStatementContext(_ctx, getState());
        enterRule(_localctx, 120, RULE_npcMoveToStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(845);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(844);
                        match(LABEL);
                    }
                }

                setState(847);
                match(T__63);
                setState(848);
                match(BYTE);
                setState(849);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveTpStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcMoveTpStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveTpStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcMoveTpStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcMoveTpStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveTpStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveTpStatementContext npcMoveTpStatement() throws RecognitionException {
        NpcMoveTpStatementContext _localctx = new NpcMoveTpStatementContext(_ctx, getState());
        enterRule(_localctx, 122, RULE_npcMoveTpStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(852);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(851);
                        match(LABEL);
                    }
                }

                setState(854);
                match(T__64);
                setState(855);
                match(BYTE);
                setState(856);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveChaseStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public NpcMoveChaseStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveChaseStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcMoveChaseStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcMoveChaseStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveChaseStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveChaseStatementContext npcMoveChaseStatement() throws RecognitionException {
        NpcMoveChaseStatementContext _localctx = new NpcMoveChaseStatementContext(_ctx, getState());
        enterRule(_localctx, 124, RULE_npcMoveChaseStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(859);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(858);
                        match(LABEL);
                    }
                }

                setState(861);
                match(T__65);
                setState(863);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == NUMBER) {
                    {
                        setState(862);
                        match(NUMBER);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveWanderStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public NpcMoveWanderStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveWanderStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcMoveWanderStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcMoveWanderStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveWanderStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveWanderStatementContext npcMoveWanderStatement() throws RecognitionException {
        NpcMoveWanderStatementContext _localctx = new NpcMoveWanderStatementContext(_ctx, getState());
        enterRule(_localctx, 126, RULE_npcMoveWanderStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(866);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(865);
                        match(LABEL);
                    }
                }

                setState(868);
                match(T__66);
                setState(873);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == BYTE) {
                    {
                        setState(869);
                        match(BYTE);
                        setState(870);
                        match(BYTE);
                        setState(871);
                        match(BYTE);
                        setState(872);
                        match(BYTE);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveTankStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcMoveTankStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveTankStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcMoveTankStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcMoveTankStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveTankStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveTankStatementContext npcMoveTankStatement() throws RecognitionException {
        NpcMoveTankStatementContext _localctx = new NpcMoveTankStatementContext(_ctx, getState());
        enterRule(_localctx, 128, RULE_npcMoveTankStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(876);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(875);
                        match(LABEL);
                    }
                }

                setState(878);
                match(T__67);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcMoveTpOffsetStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcMoveTpOffsetStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcMoveTpOffsetStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcMoveTpOffsetStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcMoveTpOffsetStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcMoveTpOffsetStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcMoveTpOffsetStatementContext npcMoveTpOffsetStatement() throws RecognitionException {
        NpcMoveTpOffsetStatementContext _localctx = new NpcMoveTpOffsetStatementContext(_ctx, getState());
        enterRule(_localctx, 130, RULE_npcMoveTpOffsetStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(881);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(880);
                        match(LABEL);
                    }
                }

                setState(883);
                match(T__68);
                setState(884);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcAnimPlayStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcAnimPlayStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcAnimPlayStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcAnimPlayStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcAnimPlayStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcAnimPlayStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcAnimPlayStatementContext npcAnimPlayStatement() throws RecognitionException {
        NpcAnimPlayStatementContext _localctx = new NpcAnimPlayStatementContext(_ctx, getState());
        enterRule(_localctx, 132, RULE_npcAnimPlayStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(887);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(886);
                        match(LABEL);
                    }
                }

                setState(889);
                match(T__69);
                setState(890);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcAnimFrameStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcAnimFrameStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcAnimFrameStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcAnimFrameStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcAnimFrameStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcAnimFrameStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcAnimFrameStatementContext npcAnimFrameStatement() throws RecognitionException {
        NpcAnimFrameStatementContext _localctx = new NpcAnimFrameStatementContext(_ctx, getState());
        enterRule(_localctx, 134, RULE_npcAnimFrameStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(893);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(892);
                        match(LABEL);
                    }
                }

                setState(895);
                match(T__70);
                setState(896);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcAnimResumeStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcAnimResumeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcAnimResumeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcAnimResumeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcAnimResumeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcAnimResumeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcAnimResumeStatementContext npcAnimResumeStatement() throws RecognitionException {
        NpcAnimResumeStatementContext _localctx = new NpcAnimResumeStatementContext(_ctx, getState());
        enterRule(_localctx, 136, RULE_npcAnimResumeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(899);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(898);
                        match(LABEL);
                    }
                }

                setState(901);
                match(T__71);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NpcAnimThrowStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NpcAnimThrowStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_npcAnimThrowStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterNpcAnimThrowStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitNpcAnimThrowStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNpcAnimThrowStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NpcAnimThrowStatementContext npcAnimThrowStatement() throws RecognitionException {
        NpcAnimThrowStatementContext _localctx = new NpcAnimThrowStatementContext(_ctx, getState());
        enterRule(_localctx, 138, RULE_npcAnimThrowStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(904);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(903);
                        match(LABEL);
                    }
                }

                setState(906);
                match(T__72);
                setState(907);
                match(BYTE);
                setState(908);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MoneySpendStatementContext extends ParserRuleContext {
        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public MoneySpendStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_moneySpendStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterMoneySpendStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitMoneySpendStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitMoneySpendStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final MoneySpendStatementContext moneySpendStatement() throws RecognitionException {
        MoneySpendStatementContext _localctx = new MoneySpendStatementContext(_ctx, getState());
        enterRule(_localctx, 140, RULE_moneySpendStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(911);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(910);
                        match(LABEL);
                    }
                }

                setState(913);
                match(T__73);
                setState(914);
                match(NUMBER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class GivePlayerItemStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public GivePlayerItemStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_givePlayerItemStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterGivePlayerItemStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitGivePlayerItemStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitGivePlayerItemStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final GivePlayerItemStatementContext givePlayerItemStatement() throws RecognitionException {
        GivePlayerItemStatementContext _localctx = new GivePlayerItemStatementContext(_ctx, getState());
        enterRule(_localctx, 142, RULE_givePlayerItemStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(917);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(916);
                        match(LABEL);
                    }
                }

                setState(919);
                match(T__74);
                setState(920);
                match(BYTE);
                setState(921);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TilesSpriteStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TilesSpriteStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_tilesSpriteStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTilesSpriteStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitTilesSpriteStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTilesSpriteStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TilesSpriteStatementContext tilesSpriteStatement() throws RecognitionException {
        TilesSpriteStatementContext _localctx = new TilesSpriteStatementContext(_ctx, getState());
        enterRule(_localctx, 144, RULE_tilesSpriteStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(924);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(923);
                        match(LABEL);
                    }
                }

                setState(926);
                match(T__75);
                setState(927);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SwapPlayerItemStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public SwapPlayerItemStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_swapPlayerItemStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterSwapPlayerItemStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitSwapPlayerItemStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitSwapPlayerItemStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SwapPlayerItemStatementContext swapPlayerItemStatement() throws RecognitionException {
        SwapPlayerItemStatementContext _localctx = new SwapPlayerItemStatementContext(_ctx, getState());
        enterRule(_localctx, 146, RULE_swapPlayerItemStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(930);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(929);
                        match(LABEL);
                    }
                }

                setState(932);
                match(T__76);
                setState(933);
                match(BYTE);
                setState(934);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class QuakeStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public QuakeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_quakeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterQuakeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitQuakeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitQuakeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final QuakeStatementContext quakeStatement() throws RecognitionException {
        QuakeStatementContext _localctx = new QuakeStatementContext(_ctx, getState());
        enterRule(_localctx, 148, RULE_quakeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(937);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(936);
                        match(LABEL);
                    }
                }

                setState(939);
                match(T__77);
                setState(940);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TextPlainStatementContext extends ParserRuleContext {
        public TextBlockContext textBlock() {
            return getRuleContext(TextBlockContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TextPlainStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_textPlainStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTextPlainStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitTextPlainStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTextPlainStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TextPlainStatementContext textPlainStatement() throws RecognitionException {
        TextPlainStatementContext _localctx = new TextPlainStatementContext(_ctx, getState());
        enterRule(_localctx, 150, RULE_textPlainStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(943);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(942);
                        match(LABEL);
                    }
                }

                setState(945);
                match(T__78);
                setState(946);
                textBlock();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TextOptionStatementContext extends ParserRuleContext {
        public TextBlockContext textBlock() {
            return getRuleContext(TextBlockContext.class, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TextOptionStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_textOptionStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTextOptionStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitTextOptionStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTextOptionStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TextOptionStatementContext textOptionStatement() throws RecognitionException {
        TextOptionStatementContext _localctx = new TextOptionStatementContext(_ctx, getState());
        enterRule(_localctx, 152, RULE_textOptionStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(949);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(948);
                        match(LABEL);
                    }
                }

                setState(951);
                match(T__79);
                setState(952);
                textBlock();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TextQuoteStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TextQuoteStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_textQuoteStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTextQuoteStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitTextQuoteStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTextQuoteStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TextQuoteStatementContext textQuoteStatement() throws RecognitionException {
        TextQuoteStatementContext _localctx = new TextQuoteStatementContext(_ctx, getState());
        enterRule(_localctx, 154, RULE_textQuoteStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(955);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(954);
                        match(LABEL);
                    }
                }

                setState(957);
                match(T__80);
                setState(958);
                match(BYTE);
                setState(959);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TextEventStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TextEventStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_textEventStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTextEventStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitTextEventStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTextEventStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TextEventStatementContext textEventStatement() throws RecognitionException {
        TextEventStatementContext _localctx = new TextEventStatementContext(_ctx, getState());
        enterRule(_localctx, 156, RULE_textEventStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(962);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(961);
                        match(LABEL);
                    }
                }

                setState(964);
                match(T__81);
                setState(965);
                match(BYTE);
                setState(966);
                match(BYTE);
                setState(967);
                match(BYTE);
                setState(968);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TextBlockContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<TerminalNode> TEXT() {
            return getTokens(SpriteScriptParser.TEXT);
        }

        public TerminalNode TEXT(int i) {
            return getToken(SpriteScriptParser.TEXT, i);
        }

        public TextBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_textBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterTextBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitTextBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTextBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TextBlockContext textBlock() throws RecognitionException {
        TextBlockContext _localctx = new TextBlockContext(_ctx, getState());
        enterRule(_localctx, 158, RULE_textBlock);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(971);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 100, _ctx)) {
                    case 1: {
                        setState(970);
                        match(LABEL);
                    }
                    break;
                }
                setState(977);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__82) {
                    {
                        {
                            setState(973);
                            match(T__82);
                            setState(974);
                            match(TEXT);
                        }
                    }
                    setState(979);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EventOpenStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public EventOpenStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_eventOpenStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterEventOpenStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitEventOpenStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitEventOpenStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EventOpenStatementContext eventOpenStatement() throws RecognitionException {
        EventOpenStatementContext _localctx = new EventOpenStatementContext(_ctx, getState());
        enterRule(_localctx, 160, RULE_eventOpenStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(981);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(980);
                        match(LABEL);
                    }
                }

                setState(983);
                match(T__83);
                setState(984);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EventCloseStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public EventCloseStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_eventCloseStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterEventCloseStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitEventCloseStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitEventCloseStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EventCloseStatementContext eventCloseStatement() throws RecognitionException {
        EventCloseStatementContext _localctx = new EventCloseStatementContext(_ctx, getState());
        enterRule(_localctx, 162, RULE_eventCloseStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(987);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(986);
                        match(LABEL);
                    }
                }

                setState(989);
                match(T__84);
                setState(990);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EventWaitStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public EventWaitStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_eventWaitStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterEventWaitStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitEventWaitStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitEventWaitStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EventWaitStatementContext eventWaitStatement() throws RecognitionException {
        EventWaitStatementContext _localctx = new EventWaitStatementContext(_ctx, getState());
        enterRule(_localctx, 164, RULE_eventWaitStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(993);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(992);
                        match(LABEL);
                    }
                }

                setState(995);
                match(T__85);
                setState(996);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TeamJoinStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TeamJoinStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_teamJoinStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTeamJoinStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitTeamJoinStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTeamJoinStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TeamJoinStatementContext teamJoinStatement() throws RecognitionException {
        TeamJoinStatementContext _localctx = new TeamJoinStatementContext(_ctx, getState());
        enterRule(_localctx, 166, RULE_teamJoinStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(999);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(998);
                        match(LABEL);
                    }
                }

                setState(1001);
                match(T__86);
                setState(1003);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__87) {
                    {
                        setState(1002);
                        match(T__87);
                    }
                }

                setState(1005);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TeamLeaveStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TeamLeaveStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_teamLeaveStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTeamLeaveStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitTeamLeaveStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTeamLeaveStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TeamLeaveStatementContext teamLeaveStatement() throws RecognitionException {
        TeamLeaveStatementContext _localctx = new TeamLeaveStatementContext(_ctx, getState());
        enterRule(_localctx, 168, RULE_teamLeaveStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1008);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1007);
                        match(LABEL);
                    }
                }

                setState(1010);
                match(T__88);
                setState(1011);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TeamHideStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public TeamHideStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_teamHideStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterTeamHideStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitTeamHideStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitTeamHideStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TeamHideStatementContext teamHideStatement() throws RecognitionException {
        TeamHideStatementContext _localctx = new TeamHideStatementContext(_ctx, getState());
        enterRule(_localctx, 170, RULE_teamHideStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1014);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1013);
                        match(LABEL);
                    }
                }

                setState(1016);
                match(T__89);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ControlChangeStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public ControlChangeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_controlChangeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterControlChangeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitControlChangeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitControlChangeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ControlChangeStatementContext controlChangeStatement() throws RecognitionException {
        ControlChangeStatementContext _localctx = new ControlChangeStatementContext(_ctx, getState());
        enterRule(_localctx, 172, RULE_controlChangeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1019);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1018);
                        match(LABEL);
                    }
                }

                setState(1021);
                match(T__90);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MenuStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public MenuStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_menuStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterMenuStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitMenuStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitMenuStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final MenuStatementContext menuStatement() throws RecognitionException {
        MenuStatementContext _localctx = new MenuStatementContext(_ctx, getState());
        enterRule(_localctx, 174, RULE_menuStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1024);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1023);
                        match(LABEL);
                    }
                }

                setState(1026);
                match(T__91);
                setState(1027);
                match(BYTE);
                setState(1028);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class JmpStatementContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(SpriteScriptParser.STRING, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public JmpStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jmpStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterJmpStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitJmpStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitJmpStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JmpStatementContext jmpStatement() throws RecognitionException {
        JmpStatementContext _localctx = new JmpStatementContext(_ctx, getState());
        enterRule(_localctx, 176, RULE_jmpStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1031);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1030);
                        match(LABEL);
                    }
                }

                setState(1033);
                match(T__92);
                setState(1034);
                match(STRING);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ScrollStatementContext extends ParserRuleContext {
        public TerminalNode DIRECTION() {
            return getToken(SpriteScriptParser.DIRECTION, 0);
        }

        public TerminalNode NUMBER() {
            return getToken(SpriteScriptParser.NUMBER, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public ScrollStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_scrollStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterScrollStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitScrollStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitScrollStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ScrollStatementContext scrollStatement() throws RecognitionException {
        ScrollStatementContext _localctx = new ScrollStatementContext(_ctx, getState());
        enterRule(_localctx, 178, RULE_scrollStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1037);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1036);
                        match(LABEL);
                    }
                }

                setState(1039);
                match(T__93);
                setState(1040);
                match(DIRECTION);
                setState(1041);
                match(NUMBER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FaceStatementContext extends ParserRuleContext {
        public TerminalNode DIRECTION() {
            return getToken(SpriteScriptParser.DIRECTION, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public FaceStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_faceStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterFaceStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitFaceStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitFaceStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FaceStatementContext faceStatement() throws RecognitionException {
        FaceStatementContext _localctx = new FaceStatementContext(_ctx, getState());
        enterRule(_localctx, 180, RULE_faceStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1044);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1043);
                        match(LABEL);
                    }
                }

                setState(1046);
                match(T__94);
                setState(1047);
                match(DIRECTION);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FaceBackStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public FaceBackStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_faceBackStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterFaceBackStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitFaceBackStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitFaceBackStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FaceBackStatementContext faceBackStatement() throws RecognitionException {
        FaceBackStatementContext _localctx = new FaceBackStatementContext(_ctx, getState());
        enterRule(_localctx, 182, RULE_faceBackStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1050);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1049);
                        match(LABEL);
                    }
                }

                setState(1052);
                match(T__95);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FacePlayerStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public FacePlayerStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_facePlayerStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterFacePlayerStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitFacePlayerStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitFacePlayerStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FacePlayerStatementContext facePlayerStatement() throws RecognitionException {
        FacePlayerStatementContext _localctx = new FacePlayerStatementContext(_ctx, getState());
        enterRule(_localctx, 184, RULE_facePlayerStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1055);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1054);
                        match(LABEL);
                    }
                }

                setState(1057);
                match(T__96);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerFaceStatementContext extends ParserRuleContext {
        public TerminalNode DIRECTION() {
            return getToken(SpriteScriptParser.DIRECTION, 0);
        }

        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerFaceStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerFaceStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerFaceStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerFaceStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerFaceStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerFaceStatementContext playerFaceStatement() throws RecognitionException {
        PlayerFaceStatementContext _localctx = new PlayerFaceStatementContext(_ctx, getState());
        enterRule(_localctx, 186, RULE_playerFaceStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1060);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1059);
                        match(LABEL);
                    }
                }

                setState(1062);
                match(T__97);
                setState(1063);
                _la = _input.LA(1);
                if (!(_la == DIRECTION || _la == BYTE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerShowStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerShowStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerShowStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerShowStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerShowStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerShowStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerShowStatementContext playerShowStatement() throws RecognitionException {
        PlayerShowStatementContext _localctx = new PlayerShowStatementContext(_ctx, getState());
        enterRule(_localctx, 188, RULE_playerShowStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1066);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1065);
                        match(LABEL);
                    }
                }

                setState(1068);
                match(T__98);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerShowCustomStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerShowCustomStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerShowCustomStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerShowCustomStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerShowCustomStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerShowCustomStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerShowCustomStatementContext playerShowCustomStatement() throws RecognitionException {
        PlayerShowCustomStatementContext _localctx = new PlayerShowCustomStatementContext(_ctx, getState());
        enterRule(_localctx, 190, RULE_playerShowCustomStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1071);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1070);
                        match(LABEL);
                    }
                }

                setState(1073);
                match(T__98);
                setState(1074);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerHideStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerHideStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerHideStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerHideStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerHideStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerHideStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerHideStatementContext playerHideStatement() throws RecognitionException {
        PlayerHideStatementContext _localctx = new PlayerHideStatementContext(_ctx, getState());
        enterRule(_localctx, 192, RULE_playerHideStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1077);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1076);
                        match(LABEL);
                    }
                }

                setState(1079);
                match(T__99);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerHideAllStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerHideAllStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerHideAllStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerHideAllStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerHideAllStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerHideAllStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerHideAllStatementContext playerHideAllStatement() throws RecognitionException {
        PlayerHideAllStatementContext _localctx = new PlayerHideAllStatementContext(_ctx, getState());
        enterRule(_localctx, 194, RULE_playerHideAllStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1082);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1081);
                        match(LABEL);
                    }
                }

                setState(1084);
                match(T__100);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerBecomeStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerBecomeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerBecomeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerBecomeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerBecomeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerBecomeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerBecomeStatementContext playerBecomeStatement() throws RecognitionException {
        PlayerBecomeStatementContext _localctx = new PlayerBecomeStatementContext(_ctx, getState());
        enterRule(_localctx, 196, RULE_playerBecomeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1087);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1086);
                        match(LABEL);
                    }
                }

                setState(1089);
                match(T__101);
                setState(1090);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerTpStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public PlayerTpStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerTpStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerTpStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitPlayerTpStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerTpStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerTpStatementContext playerTpStatement() throws RecognitionException {
        PlayerTpStatementContext _localctx = new PlayerTpStatementContext(_ctx, getState());
        enterRule(_localctx, 198, RULE_playerTpStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1093);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1092);
                        match(LABEL);
                    }
                }

                setState(1095);
                match(T__102);
                setState(1102);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == BYTE) {
                    {
                        setState(1096);
                        match(BYTE);
                        setState(1097);
                        match(BYTE);
                        setState(1098);
                        match(BYTE);
                        setState(1100);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la == T__103) {
                            {
                                setState(1099);
                                match(T__103);
                            }
                        }

                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerPullStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerPullStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerPullStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerPullStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerPullStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerPullStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerPullStatementContext playerPullStatement() throws RecognitionException {
        PlayerPullStatementContext _localctx = new PlayerPullStatementContext(_ctx, getState());
        enterRule(_localctx, 200, RULE_playerPullStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1105);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1104);
                        match(LABEL);
                    }
                }

                setState(1107);
                match(T__104);
                setState(1108);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PlayerUnpullStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public PlayerUnpullStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_playerUnpullStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterPlayerUnpullStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitPlayerUnpullStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitPlayerUnpullStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PlayerUnpullStatementContext playerUnpullStatement() throws RecognitionException {
        PlayerUnpullStatementContext _localctx = new PlayerUnpullStatementContext(_ctx, getState());
        enterRule(_localctx, 202, RULE_playerUnpullStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1111);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1110);
                        match(LABEL);
                    }
                }

                setState(1113);
                match(T__105);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BattleStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public BattleStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_battleStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterBattleStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitBattleStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitBattleStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BattleStatementContext battleStatement() throws RecognitionException {
        BattleStatementContext _localctx = new BattleStatementContext(_ctx, getState());
        enterRule(_localctx, 204, RULE_battleStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1116);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1115);
                        match(LABEL);
                    }
                }

                setState(1118);
                match(T__106);
                setState(1119);
                match(BYTE);
                setState(1120);
                match(BYTE);
                setState(1121);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class WaitTimeStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public WaitTimeStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_waitTimeStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterWaitTimeStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitWaitTimeStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitWaitTimeStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final WaitTimeStatementContext waitTimeStatement() throws RecognitionException {
        WaitTimeStatementContext _localctx = new WaitTimeStatementContext(_ctx, getState());
        enterRule(_localctx, 206, RULE_waitTimeStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1124);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1123);
                        match(LABEL);
                    }
                }

                setState(1126);
                match(T__107);
                setState(1127);
                _la = _input.LA(1);
                if (!(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & 134217735L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class WaitEventStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public WaitEventStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_waitEventStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterWaitEventStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitWaitEventStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitWaitEventStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final WaitEventStatementContext waitEventStatement() throws RecognitionException {
        WaitEventStatementContext _localctx = new WaitEventStatementContext(_ctx, getState());
        enterRule(_localctx, 208, RULE_waitEventStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1130);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1129);
                        match(LABEL);
                    }
                }

                setState(1132);
                match(T__111);
                setState(1133);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FlashScreenStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public FlashScreenStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_flashScreenStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterFlashScreenStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitFlashScreenStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitFlashScreenStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FlashScreenStatementContext flashScreenStatement() throws RecognitionException {
        FlashScreenStatementContext _localctx = new FlashScreenStatementContext(_ctx, getState());
        enterRule(_localctx, 210, RULE_flashScreenStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1136);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1135);
                        match(LABEL);
                    }
                }

                setState(1138);
                match(T__112);
                setState(1139);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ConveyorStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public ConveyorStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conveyorStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterConveyorStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitConveyorStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitConveyorStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConveyorStatementContext conveyorStatement() throws RecognitionException {
        ConveyorStatementContext _localctx = new ConveyorStatementContext(_ctx, getState());
        enterRule(_localctx, 212, RULE_conveyorStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1142);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1141);
                        match(LABEL);
                    }
                }

                setState(1144);
                match(T__113);
                setState(1145);
                _la = _input.LA(1);
                if (!(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & 7L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ChangeTankMapStatementContext extends ParserRuleContext {
        public List<TerminalNode> BYTE() {
            return getTokens(SpriteScriptParser.BYTE);
        }

        public TerminalNode BYTE(int i) {
            return getToken(SpriteScriptParser.BYTE, i);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public ChangeTankMapStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_changeTankMapStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).enterChangeTankMapStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener)
                ((SpriteScriptListener) listener).exitChangeTankMapStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitChangeTankMapStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ChangeTankMapStatementContext changeTankMapStatement() throws RecognitionException {
        ChangeTankMapStatementContext _localctx = new ChangeTankMapStatementContext(_ctx, getState());
        enterRule(_localctx, 214, RULE_changeTankMapStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1148);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1147);
                        match(LABEL);
                    }
                }

                setState(1150);
                match(T__117);
                setState(1151);
                match(BYTE);
                setState(1152);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class RecoverStatementContext extends ParserRuleContext {
        public TerminalNode HEX() {
            return getToken(SpriteScriptParser.HEX, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public RecoverStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_recoverStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterRecoverStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitRecoverStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitRecoverStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final RecoverStatementContext recoverStatement() throws RecognitionException {
        RecoverStatementContext _localctx = new RecoverStatementContext(_ctx, getState());
        enterRule(_localctx, 216, RULE_recoverStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1155);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1154);
                        match(LABEL);
                    }
                }

                setState(1157);
                match(T__118);
                setState(1158);
                match(HEX);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SleepStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public SleepStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sleepStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterSleepStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitSleepStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitSleepStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SleepStatementContext sleepStatement() throws RecognitionException {
        SleepStatementContext _localctx = new SleepStatementContext(_ctx, getState());
        enterRule(_localctx, 218, RULE_sleepStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1161);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1160);
                        match(LABEL);
                    }
                }

                setState(1163);
                match(T__119);
                setState(1164);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MusicStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public MusicStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_musicStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterMusicStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitMusicStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitMusicStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final MusicStatementContext musicStatement() throws RecognitionException {
        MusicStatementContext _localctx = new MusicStatementContext(_ctx, getState());
        enterRule(_localctx, 220, RULE_musicStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1167);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1166);
                        match(LABEL);
                    }
                }

                setState(1169);
                match(T__120);
                setState(1170);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NextdayStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public NextdayStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nextdayStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterNextdayStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitNextdayStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitNextdayStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NextdayStatementContext nextdayStatement() throws RecognitionException {
        NextdayStatementContext _localctx = new NextdayStatementContext(_ctx, getState());
        enterRule(_localctx, 222, RULE_nextdayStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1173);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1172);
                        match(LABEL);
                    }
                }

                setState(1175);
                match(T__121);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SpeakerStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public SpeakerStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_speakerStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterSpeakerStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitSpeakerStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitSpeakerStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SpeakerStatementContext speakerStatement() throws RecognitionException {
        SpeakerStatementContext _localctx = new SpeakerStatementContext(_ctx, getState());
        enterRule(_localctx, 224, RULE_speakerStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1178);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1177);
                        match(LABEL);
                    }
                }

                setState(1180);
                match(T__122);
                setState(1181);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SceneStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public SceneStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sceneStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterSceneStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitSceneStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitSceneStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SceneStatementContext sceneStatement() throws RecognitionException {
        SceneStatementContext _localctx = new SceneStatementContext(_ctx, getState());
        enterRule(_localctx, 226, RULE_sceneStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1184);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1183);
                        match(LABEL);
                    }
                }

                setState(1186);
                match(T__123);
                setState(1187);
                _la = _input.LA(1);
                if (!(_la == T__124 || _la == BYTE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SystemStatementContext extends ParserRuleContext {
        public TerminalNode BYTE() {
            return getToken(SpriteScriptParser.BYTE, 0);
        }

        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public SystemStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_systemStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterSystemStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitSystemStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitSystemStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SystemStatementContext systemStatement() throws RecognitionException {
        SystemStatementContext _localctx = new SystemStatementContext(_ctx, getState());
        enterRule(_localctx, 228, RULE_systemStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1190);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1189);
                        match(LABEL);
                    }
                }

                setState(1192);
                match(T__125);
                setState(1193);
                match(BYTE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EndStatementContext extends ParserRuleContext {
        public TerminalNode LABEL() {
            return getToken(SpriteScriptParser.LABEL, 0);
        }

        public EndStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_endStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterEndStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitEndStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitEndStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EndStatementContext endStatement() throws RecognitionException {
        EndStatementContext _localctx = new EndStatementContext(_ctx, getState());
        enterRule(_localctx, 230, RULE_endStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1196);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LABEL) {
                    {
                        setState(1195);
                        match(LABEL);
                    }
                }

                setState(1198);
                match(T__126);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DefineStatementContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(SpriteScriptParser.STRING, 0);
        }

        public TerminalNode TEXT() {
            return getToken(SpriteScriptParser.TEXT, 0);
        }

        public DefineStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_defineStatement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).enterDefineStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof SpriteScriptListener) ((SpriteScriptListener) listener).exitDefineStatement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SpriteScriptVisitor)
                return ((SpriteScriptVisitor<? extends T>) visitor).visitDefineStatement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DefineStatementContext defineStatement() throws RecognitionException {
        DefineStatementContext _localctx = new DefineStatementContext(_ctx, getState());
        enterRule(_localctx, 232, RULE_defineStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1200);
                match(T__127);
                setState(1201);
                match(STRING);
                setState(1202);
                match(TEXT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\u0004\u0001\u008b\u04b5\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001" +
                    "\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004" +
                    "\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007" +
                    "\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b" +
                    "\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007" +
                    "\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007" +
                    "\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007" +
                    "\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007" +
                    "\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007" +
                    "\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007" +
                    "\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007" +
                    "\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007" +
                    "\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007" +
                    ",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007" +
                    "1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007" +
                    "6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007" +
                    ";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007" +
                    "@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007" +
                    "E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007" +
                    "J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007" +
                    "O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007" +
                    "T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007" +
                    "Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007" +
                    "^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007" +
                    "c\u0002d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007" +
                    "h\u0002i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007" +
                    "m\u0002n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007" +
                    "r\u0002s\u0007s\u0002t\u0007t\u0001\u0000\u0001\u0000\u0005\u0000\u00ed" +
                    "\b\u0000\n\u0000\f\u0000\u00f0\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0003\u0001\u0163\b\u0001\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0001\u0003\u0003\u0003\u0169\b\u0003\u0001\u0003\u0001\u0003" +
                    "\u0001\u0004\u0003\u0004\u016e\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005" +
                    "\u0003\u0005\u0173\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005" +
                    "\u0178\b\u0005\n\u0005\f\u0005\u017b\t\u0005\u0001\u0006\u0003\u0006\u017e" +
                    "\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u0183\b\u0006" +
                    "\n\u0006\f\u0006\u0186\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0003" +
                    "\u0007\u018b\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001" +
                    "\b\u0003\b\u0192\b\b\u0001\b\u0001\b\u0001\b\u0005\b\u0197\b\b\n\b\f\b" +
                    "\u019a\t\b\u0001\b\u0001\b\u0001\t\u0003\t\u019f\b\t\u0001\t\u0001\t\u0001" +
                    "\t\u0001\t\u0001\n\u0003\n\u01a6\b\n\u0001\n\u0001\n\u0001\n\u0005\n\u01ab" +
                    "\b\n\n\n\f\n\u01ae\t\n\u0001\n\u0001\n\u0001\u000b\u0003\u000b\u01b3\b" +
                    "\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0003\f" +
                    "\u01ba\b\f\u0001\f\u0001\f\u0005\f\u01be\b\f\n\f\f\f\u01c1\t\f\u0001\f" +
                    "\u0001\f\u0001\r\u0003\r\u01c6\b\r\u0001\r\u0001\r\u0001\r\u0001\u000e" +
                    "\u0003\u000e\u01cc\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e" +
                    "\u01d1\b\u000e\n\u000e\f\u000e\u01d4\t\u000e\u0001\u000e\u0001\u000e\u0001" +
                    "\u000f\u0003\u000f\u01d9\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001" +
                    "\u000f\u0001\u0010\u0003\u0010\u01e0\b\u0010\u0001\u0010\u0001\u0010\u0001" +
                    "\u0010\u0005\u0010\u01e5\b\u0010\n\u0010\f\u0010\u01e8\t\u0010\u0001\u0010" +
                    "\u0001\u0010\u0001\u0011\u0003\u0011\u01ed\b\u0011\u0001\u0011\u0001\u0011" +
                    "\u0001\u0011\u0001\u0011\u0001\u0012\u0003\u0012\u01f4\b\u0012\u0001\u0012" +
                    "\u0001\u0012\u0005\u0012\u01f8\b\u0012\n\u0012\f\u0012\u01fb\t\u0012\u0001" +
                    "\u0012\u0001\u0012\u0001\u0013\u0003\u0013\u0200\b\u0013\u0001\u0013\u0001" +
                    "\u0013\u0001\u0013\u0001\u0014\u0003\u0014\u0206\b\u0014\u0001\u0014\u0001" +
                    "\u0014\u0001\u0014\u0005\u0014\u020b\b\u0014\n\u0014\f\u0014\u020e\t\u0014" +
                    "\u0001\u0014\u0001\u0014\u0001\u0015\u0003\u0015\u0213\b\u0015\u0001\u0015" +
                    "\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0003\u0016\u021a\b\u0016" +
                    "\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u021f\b\u0016\n\u0016" +
                    "\f\u0016\u0222\t\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0003\u0017" +
                    "\u0227\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018" +
                    "\u0003\u0018\u022e\b\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018" +
                    "\u0233\b\u0018\n\u0018\f\u0018\u0236\t\u0018\u0001\u0018\u0001\u0018\u0001" +
                    "\u0019\u0003\u0019\u023b\b\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001" +
                    "\u0019\u0001\u001a\u0003\u001a\u0242\b\u001a\u0001\u001a\u0001\u001a\u0001" +
                    "\u001a\u0001\u001a\u0005\u001a\u0248\b\u001a\n\u001a\f\u001a\u024b\t\u001a" +
                    "\u0001\u001a\u0001\u001a\u0001\u001b\u0003\u001b\u0250\b\u001b\u0001\u001b" +
                    "\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0003\u001c" +
                    "\u0258\b\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c" +
                    "\u0001\u001c\u0005\u001c\u0260\b\u001c\n\u001c\f\u001c\u0263\t\u001c\u0001" +
                    "\u001c\u0001\u001c\u0001\u001d\u0003\u001d\u0268\b\u001d\u0001\u001d\u0001" +
                    "\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001" +
                    "\u001e\u0003\u001e\u0272\b\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005" +
                    "\u001e\u0277\b\u001e\n\u001e\f\u001e\u027a\t\u001e\u0001\u001e\u0001\u001e" +
                    "\u0001\u001f\u0003\u001f\u027f\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f" +
                    "\u0001\u001f\u0001 \u0003 \u0286\b \u0001 \u0001 \u0001 \u0005 \u028b" +
                    "\b \n \f \u028e\t \u0001 \u0001 \u0001!\u0003!\u0293\b!\u0001!\u0001!" +
                    "\u0001!\u0001!\u0001\"\u0003\"\u029a\b\"\u0001\"\u0001\"\u0001\"\u0005" +
                    "\"\u029f\b\"\n\"\f\"\u02a2\t\"\u0001\"\u0001\"\u0001#\u0003#\u02a7\b#" +
                    "\u0001#\u0001#\u0001#\u0001#\u0001$\u0003$\u02ae\b$\u0001$\u0001$\u0005" +
                    "$\u02b2\b$\n$\f$\u02b5\t$\u0001$\u0001$\u0001%\u0003%\u02ba\b%\u0001%" +
                    "\u0001%\u0001%\u0001&\u0003&\u02c0\b&\u0001&\u0001&\u0001&\u0005&\u02c5" +
                    "\b&\n&\f&\u02c8\t&\u0001&\u0001&\u0001\'\u0003\'\u02cd\b\'\u0001\'\u0001" +
                    "\'\u0001\'\u0001\'\u0001(\u0003(\u02d4\b(\u0001(\u0001(\u0005(\u02d8\b" +
                    "(\n(\f(\u02db\t(\u0001(\u0001(\u0001)\u0003)\u02e0\b)\u0001)\u0001)\u0001" +
                    ")\u0001*\u0001*\u0001+\u0001+\u0001,\u0003,\u02ea\b,\u0001,\u0001,\u0005" +
                    ",\u02ee\b,\n,\f,\u02f1\t,\u0001,\u0001,\u0001,\u0001-\u0003-\u02f7\b-" +
                    "\u0001-\u0001-\u0001-\u0003-\u02fc\b-\u0001.\u0003.\u02ff\b.\u0001.\u0001" +
                    ".\u0001.\u0001/\u0003/\u0305\b/\u0001/\u0001/\u00010\u00030\u030a\b0\u0001" +
                    "0\u00010\u00010\u00011\u00031\u0310\b1\u00011\u00011\u00012\u00032\u0315" +
                    "\b2\u00012\u00012\u00012\u00013\u00033\u031b\b3\u00013\u00013\u00013\u0001" +
                    "4\u00034\u0321\b4\u00014\u00014\u00014\u00015\u00035\u0327\b5\u00015\u0001" +
                    "5\u00016\u00036\u032c\b6\u00016\u00016\u00017\u00037\u0331\b7\u00017\u0001" +
                    "7\u00018\u00038\u0336\b8\u00018\u00018\u00018\u00019\u00039\u033c\b9\u0001" +
                    "9\u00019\u0001:\u0003:\u0341\b:\u0001:\u0001:\u0001;\u0003;\u0346\b;\u0001" +
                    ";\u0001;\u0001;\u0003;\u034b\b;\u0001<\u0003<\u034e\b<\u0001<\u0001<\u0001" +
                    "<\u0001<\u0001=\u0003=\u0355\b=\u0001=\u0001=\u0001=\u0001=\u0001>\u0003" +
                    ">\u035c\b>\u0001>\u0001>\u0003>\u0360\b>\u0001?\u0003?\u0363\b?\u0001" +
                    "?\u0001?\u0001?\u0001?\u0001?\u0003?\u036a\b?\u0001@\u0003@\u036d\b@\u0001" +
                    "@\u0001@\u0001A\u0003A\u0372\bA\u0001A\u0001A\u0001A\u0001B\u0003B\u0378" +
                    "\bB\u0001B\u0001B\u0001B\u0001C\u0003C\u037e\bC\u0001C\u0001C\u0001C\u0001" +
                    "D\u0003D\u0384\bD\u0001D\u0001D\u0001E\u0003E\u0389\bE\u0001E\u0001E\u0001" +
                    "E\u0001E\u0001F\u0003F\u0390\bF\u0001F\u0001F\u0001F\u0001G\u0003G\u0396" +
                    "\bG\u0001G\u0001G\u0001G\u0001G\u0001H\u0003H\u039d\bH\u0001H\u0001H\u0001" +
                    "H\u0001I\u0003I\u03a3\bI\u0001I\u0001I\u0001I\u0001I\u0001J\u0003J\u03aa" +
                    "\bJ\u0001J\u0001J\u0001J\u0001K\u0003K\u03b0\bK\u0001K\u0001K\u0001K\u0001" +
                    "L\u0003L\u03b6\bL\u0001L\u0001L\u0001L\u0001M\u0003M\u03bc\bM\u0001M\u0001" +
                    "M\u0001M\u0001M\u0001N\u0003N\u03c3\bN\u0001N\u0001N\u0001N\u0001N\u0001" +
                    "N\u0001N\u0001O\u0003O\u03cc\bO\u0001O\u0001O\u0005O\u03d0\bO\nO\fO\u03d3" +
                    "\tO\u0001P\u0003P\u03d6\bP\u0001P\u0001P\u0001P\u0001Q\u0003Q\u03dc\b" +
                    "Q\u0001Q\u0001Q\u0001Q\u0001R\u0003R\u03e2\bR\u0001R\u0001R\u0001R\u0001" +
                    "S\u0003S\u03e8\bS\u0001S\u0001S\u0003S\u03ec\bS\u0001S\u0001S\u0001T\u0003" +
                    "T\u03f1\bT\u0001T\u0001T\u0001T\u0001U\u0003U\u03f7\bU\u0001U\u0001U\u0001" +
                    "V\u0003V\u03fc\bV\u0001V\u0001V\u0001W\u0003W\u0401\bW\u0001W\u0001W\u0001" +
                    "W\u0001W\u0001X\u0003X\u0408\bX\u0001X\u0001X\u0001X\u0001Y\u0003Y\u040e" +
                    "\bY\u0001Y\u0001Y\u0001Y\u0001Y\u0001Z\u0003Z\u0415\bZ\u0001Z\u0001Z\u0001" +
                    "Z\u0001[\u0003[\u041b\b[\u0001[\u0001[\u0001\\\u0003\\\u0420\b\\\u0001" +
                    "\\\u0001\\\u0001]\u0003]\u0425\b]\u0001]\u0001]\u0001]\u0001^\u0003^\u042b" +
                    "\b^\u0001^\u0001^\u0001_\u0003_\u0430\b_\u0001_\u0001_\u0001_\u0001`\u0003" +
                    "`\u0436\b`\u0001`\u0001`\u0001a\u0003a\u043b\ba\u0001a\u0001a\u0001b\u0003" +
                    "b\u0440\bb\u0001b\u0001b\u0001b\u0001c\u0003c\u0446\bc\u0001c\u0001c\u0001" +
                    "c\u0001c\u0001c\u0003c\u044d\bc\u0003c\u044f\bc\u0001d\u0003d\u0452\b" +
                    "d\u0001d\u0001d\u0001d\u0001e\u0003e\u0458\be\u0001e\u0001e\u0001f\u0003" +
                    "f\u045d\bf\u0001f\u0001f\u0001f\u0001f\u0001f\u0001g\u0003g\u0465\bg\u0001" +
                    "g\u0001g\u0001g\u0001h\u0003h\u046b\bh\u0001h\u0001h\u0001h\u0001i\u0003" +
                    "i\u0471\bi\u0001i\u0001i\u0001i\u0001j\u0003j\u0477\bj\u0001j\u0001j\u0001" +
                    "j\u0001k\u0003k\u047d\bk\u0001k\u0001k\u0001k\u0001k\u0001l\u0003l\u0484" +
                    "\bl\u0001l\u0001l\u0001l\u0001m\u0003m\u048a\bm\u0001m\u0001m\u0001m\u0001" +
                    "n\u0003n\u0490\bn\u0001n\u0001n\u0001n\u0001o\u0003o\u0496\bo\u0001o\u0001" +
                    "o\u0001p\u0003p\u049b\bp\u0001p\u0001p\u0001p\u0001q\u0003q\u04a1\bq\u0001" +
                    "q\u0001q\u0001q\u0001r\u0003r\u04a7\br\u0001r\u0001r\u0001r\u0001s\u0003" +
                    "s\u04ad\bs\u0001s\u0001s\u0001t\u0001t\u0001t\u0001t\u0001t\u0000\u0000" +
                    "u\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a" +
                    "\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082" +
                    "\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a" +
                    "\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2" +
                    "\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca" +
                    "\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2" +
                    "\u00e4\u00e6\u00e8\u0000\u0007\u0001\u0000\u0002\u0003\u0001\u0000+,\u0001" +
                    "\u000001\u0002\u0000\u0081\u0081\u0088\u0088\u0002\u0000mo\u0088\u0088" +
                    "\u0001\u0000su\u0002\u0000}}\u0088\u0088\u0539\u0000\u00ea\u0001\u0000" +
                    "\u0000\u0000\u0002\u0162\u0001\u0000\u0000\u0000\u0004\u0164\u0001\u0000" +
                    "\u0000\u0000\u0006\u0168\u0001\u0000\u0000\u0000\b\u016d\u0001\u0000\u0000" +
                    "\u0000\n\u0172\u0001\u0000\u0000\u0000\f\u017d\u0001\u0000\u0000\u0000" +
                    "\u000e\u018a\u0001\u0000\u0000\u0000\u0010\u0191\u0001\u0000\u0000\u0000" +
                    "\u0012\u019e\u0001\u0000\u0000\u0000\u0014\u01a5\u0001\u0000\u0000\u0000" +
                    "\u0016\u01b2\u0001\u0000\u0000\u0000\u0018\u01b9\u0001\u0000\u0000\u0000" +
                    "\u001a\u01c5\u0001\u0000\u0000\u0000\u001c\u01cb\u0001\u0000\u0000\u0000" +
                    "\u001e\u01d8\u0001\u0000\u0000\u0000 \u01df\u0001\u0000\u0000\u0000\"" +
                    "\u01ec\u0001\u0000\u0000\u0000$\u01f3\u0001\u0000\u0000\u0000&\u01ff\u0001" +
                    "\u0000\u0000\u0000(\u0205\u0001\u0000\u0000\u0000*\u0212\u0001\u0000\u0000" +
                    "\u0000,\u0219\u0001\u0000\u0000\u0000.\u0226\u0001\u0000\u0000\u00000" +
                    "\u022d\u0001\u0000\u0000\u00002\u023a\u0001\u0000\u0000\u00004\u0241\u0001" +
                    "\u0000\u0000\u00006\u024f\u0001\u0000\u0000\u00008\u0257\u0001\u0000\u0000" +
                    "\u0000:\u0267\u0001\u0000\u0000\u0000<\u0271\u0001\u0000\u0000\u0000>" +
                    "\u027e\u0001\u0000\u0000\u0000@\u0285\u0001\u0000\u0000\u0000B\u0292\u0001" +
                    "\u0000\u0000\u0000D\u0299\u0001\u0000\u0000\u0000F\u02a6\u0001\u0000\u0000" +
                    "\u0000H\u02ad\u0001\u0000\u0000\u0000J\u02b9\u0001\u0000\u0000\u0000L" +
                    "\u02bf\u0001\u0000\u0000\u0000N\u02cc\u0001\u0000\u0000\u0000P\u02d3\u0001" +
                    "\u0000\u0000\u0000R\u02df\u0001\u0000\u0000\u0000T\u02e4\u0001\u0000\u0000" +
                    "\u0000V\u02e6\u0001\u0000\u0000\u0000X\u02e9\u0001\u0000\u0000\u0000Z" +
                    "\u02f6\u0001\u0000\u0000\u0000\\\u02fe\u0001\u0000\u0000\u0000^\u0304" +
                    "\u0001\u0000\u0000\u0000`\u0309\u0001\u0000\u0000\u0000b\u030f\u0001\u0000" +
                    "\u0000\u0000d\u0314\u0001\u0000\u0000\u0000f\u031a\u0001\u0000\u0000\u0000" +
                    "h\u0320\u0001\u0000\u0000\u0000j\u0326\u0001\u0000\u0000\u0000l\u032b" +
                    "\u0001\u0000\u0000\u0000n\u0330\u0001\u0000\u0000\u0000p\u0335\u0001\u0000" +
                    "\u0000\u0000r\u033b\u0001\u0000\u0000\u0000t\u0340\u0001\u0000\u0000\u0000" +
                    "v\u0345\u0001\u0000\u0000\u0000x\u034d\u0001\u0000\u0000\u0000z\u0354" +
                    "\u0001\u0000\u0000\u0000|\u035b\u0001\u0000\u0000\u0000~\u0362\u0001\u0000" +
                    "\u0000\u0000\u0080\u036c\u0001\u0000\u0000\u0000\u0082\u0371\u0001\u0000" +
                    "\u0000\u0000\u0084\u0377\u0001\u0000\u0000\u0000\u0086\u037d\u0001\u0000" +
                    "\u0000\u0000\u0088\u0383\u0001\u0000\u0000\u0000\u008a\u0388\u0001\u0000" +
                    "\u0000\u0000\u008c\u038f\u0001\u0000\u0000\u0000\u008e\u0395\u0001\u0000" +
                    "\u0000\u0000\u0090\u039c\u0001\u0000\u0000\u0000\u0092\u03a2\u0001\u0000" +
                    "\u0000\u0000\u0094\u03a9\u0001\u0000\u0000\u0000\u0096\u03af\u0001\u0000" +
                    "\u0000\u0000\u0098\u03b5\u0001\u0000\u0000\u0000\u009a\u03bb\u0001\u0000" +
                    "\u0000\u0000\u009c\u03c2\u0001\u0000\u0000\u0000\u009e\u03cb\u0001\u0000" +
                    "\u0000\u0000\u00a0\u03d5\u0001\u0000\u0000\u0000\u00a2\u03db\u0001\u0000" +
                    "\u0000\u0000\u00a4\u03e1\u0001\u0000\u0000\u0000\u00a6\u03e7\u0001\u0000" +
                    "\u0000\u0000\u00a8\u03f0\u0001\u0000\u0000\u0000\u00aa\u03f6\u0001\u0000" +
                    "\u0000\u0000\u00ac\u03fb\u0001\u0000\u0000\u0000\u00ae\u0400\u0001\u0000" +
                    "\u0000\u0000\u00b0\u0407\u0001\u0000\u0000\u0000\u00b2\u040d\u0001\u0000" +
                    "\u0000\u0000\u00b4\u0414\u0001\u0000\u0000\u0000\u00b6\u041a\u0001\u0000" +
                    "\u0000\u0000\u00b8\u041f\u0001\u0000\u0000\u0000\u00ba\u0424\u0001\u0000" +
                    "\u0000\u0000\u00bc\u042a\u0001\u0000\u0000\u0000\u00be\u042f\u0001\u0000" +
                    "\u0000\u0000\u00c0\u0435\u0001\u0000\u0000\u0000\u00c2\u043a\u0001\u0000" +
                    "\u0000\u0000\u00c4\u043f\u0001\u0000\u0000\u0000\u00c6\u0445\u0001\u0000" +
                    "\u0000\u0000\u00c8\u0451\u0001\u0000\u0000\u0000\u00ca\u0457\u0001\u0000" +
                    "\u0000\u0000\u00cc\u045c\u0001\u0000\u0000\u0000\u00ce\u0464\u0001\u0000" +
                    "\u0000\u0000\u00d0\u046a\u0001\u0000\u0000\u0000\u00d2\u0470\u0001\u0000" +
                    "\u0000\u0000\u00d4\u0476\u0001\u0000\u0000\u0000\u00d6\u047c\u0001\u0000" +
                    "\u0000\u0000\u00d8\u0483\u0001\u0000\u0000\u0000\u00da\u0489\u0001\u0000" +
                    "\u0000\u0000\u00dc\u048f\u0001\u0000\u0000\u0000\u00de\u0495\u0001\u0000" +
                    "\u0000\u0000\u00e0\u049a\u0001\u0000\u0000\u0000\u00e2\u04a0\u0001\u0000" +
                    "\u0000\u0000\u00e4\u04a6\u0001\u0000\u0000\u0000\u00e6\u04ac\u0001\u0000" +
                    "\u0000\u0000\u00e8\u04b0\u0001\u0000\u0000\u0000\u00ea\u00ee\u0003\u0004" +
                    "\u0002\u0000\u00eb\u00ed\u0003\u0002\u0001\u0000\u00ec\u00eb\u0001\u0000" +
                    "\u0000\u0000\u00ed\u00f0\u0001\u0000\u0000\u0000\u00ee\u00ec\u0001\u0000" +
                    "\u0000\u0000\u00ee\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f1\u0001\u0000" +
                    "\u0000\u0000\u00f0\u00ee\u0001\u0000\u0000\u0000\u00f1\u00f2\u0005\u0000" +
                    "\u0000\u0001\u00f2\u0001\u0001\u0000\u0000\u0000\u00f3\u0163\u0003\u00e8" +
                    "t\u0000\u00f4\u0163\u0003\n\u0005\u0000\u00f5\u0163\u0003\u0006\u0003" +
                    "\u0000\u00f6\u0163\u0003\b\u0004\u0000\u00f7\u0163\u0003\u0096K\u0000" +
                    "\u00f8\u0163\u0003\u0098L\u0000\u00f9\u0163\u0003\u009aM\u0000\u00fa\u0163" +
                    "\u0003\u009cN\u0000\u00fb\u0163\u0003\f\u0006\u0000\u00fc\u0163\u0003" +
                    "\u000e\u0007\u0000\u00fd\u0163\u0003\u0010\b\u0000\u00fe\u0163\u0003\u0012" +
                    "\t\u0000\u00ff\u0163\u0003\u0014\n\u0000\u0100\u0163\u0003\u0016\u000b" +
                    "\u0000\u0101\u0163\u0003\u0018\f\u0000\u0102\u0163\u0003\u001a\r\u0000" +
                    "\u0103\u0163\u0003\u001c\u000e\u0000\u0104\u0163\u0003\u001e\u000f\u0000" +
                    "\u0105\u0163\u0003 \u0010\u0000\u0106\u0163\u0003\"\u0011\u0000\u0107" +
                    "\u0163\u0003$\u0012\u0000\u0108\u0163\u0003&\u0013\u0000\u0109\u0163\u0003" +
                    "(\u0014\u0000\u010a\u0163\u0003*\u0015\u0000\u010b\u0163\u0003,\u0016" +
                    "\u0000\u010c\u0163\u0003.\u0017\u0000\u010d\u0163\u00030\u0018\u0000\u010e" +
                    "\u0163\u00032\u0019\u0000\u010f\u0163\u00034\u001a\u0000\u0110\u0163\u0003" +
                    "6\u001b\u0000\u0111\u0163\u00038\u001c\u0000\u0112\u0163\u0003:\u001d" +
                    "\u0000\u0113\u0163\u0003<\u001e\u0000\u0114\u0163\u0003>\u001f\u0000\u0115" +
                    "\u0163\u0003@ \u0000\u0116\u0163\u0003B!\u0000\u0117\u0163\u0003D\"\u0000" +
                    "\u0118\u0163\u0003F#\u0000\u0119\u0163\u0003H$\u0000\u011a\u0163\u0003" +
                    "J%\u0000\u011b\u0163\u0003L&\u0000\u011c\u0163\u0003N\'\u0000\u011d\u0163" +
                    "\u0003P(\u0000\u011e\u0163\u0003R)\u0000\u011f\u0163\u0003X,\u0000\u0120" +
                    "\u0163\u0003Z-\u0000\u0121\u0163\u0003\\.\u0000\u0122\u0163\u0003\u00a0" +
                    "P\u0000\u0123\u0163\u0003\u00a2Q\u0000\u0124\u0163\u0003\u00a4R\u0000" +
                    "\u0125\u0163\u0003\u00a6S\u0000\u0126\u0163\u0003\u00a8T\u0000\u0127\u0163" +
                    "\u0003\u00aaU\u0000\u0128\u0163\u0003\u00acV\u0000\u0129\u0163\u0003\u00ae" +
                    "W\u0000\u012a\u0163\u0003\u00b0X\u0000\u012b\u0163\u0003\u00b2Y\u0000" +
                    "\u012c\u0163\u0003\u00b4Z\u0000\u012d\u0163\u0003\u00b6[\u0000\u012e\u0163" +
                    "\u0003\u00b8\\\u0000\u012f\u0163\u0003\u00ba]\u0000\u0130\u0163\u0003" +
                    "\u00bc^\u0000\u0131\u0163\u0003\u00be_\u0000\u0132\u0163\u0003\u00c0`" +
                    "\u0000\u0133\u0163\u0003\u00c2a\u0000\u0134\u0163\u0003\u00c4b\u0000\u0135" +
                    "\u0163\u0003\u00c6c\u0000\u0136\u0163\u0003\u00c8d\u0000\u0137\u0163\u0003" +
                    "\u00cae\u0000\u0138\u0163\u0003\u00ccf\u0000\u0139\u0163\u0003^/\u0000" +
                    "\u013a\u0163\u0003`0\u0000\u013b\u0163\u0003b1\u0000\u013c\u0163\u0003" +
                    "d2\u0000\u013d\u0163\u0003f3\u0000\u013e\u0163\u0003h4\u0000\u013f\u0163" +
                    "\u0003j5\u0000\u0140\u0163\u0003l6\u0000\u0141\u0163\u0003n7\u0000\u0142" +
                    "\u0163\u0003p8\u0000\u0143\u0163\u0003r9\u0000\u0144\u0163\u0003t:\u0000" +
                    "\u0145\u0163\u0003v;\u0000\u0146\u0163\u0003x<\u0000\u0147\u0163\u0003" +
                    "z=\u0000\u0148\u0163\u0003|>\u0000\u0149\u0163\u0003~?\u0000\u014a\u0163" +
                    "\u0003\u0080@\u0000\u014b\u0163\u0003\u0082A\u0000\u014c\u0163\u0003\u0084" +
                    "B\u0000\u014d\u0163\u0003\u0086C\u0000\u014e\u0163\u0003\u0088D\u0000" +
                    "\u014f\u0163\u0003\u008aE\u0000\u0150\u0163\u0003\u008cF\u0000\u0151\u0163" +
                    "\u0003\u008eG\u0000\u0152\u0163\u0003\u0090H\u0000\u0153\u0163\u0003\u0092" +
                    "I\u0000\u0154\u0163\u0003\u0094J\u0000\u0155\u0163\u0003\u00ceg\u0000" +
                    "\u0156\u0163\u0003\u00d0h\u0000\u0157\u0163\u0003\u00d2i\u0000\u0158\u0163" +
                    "\u0003\u00d4j\u0000\u0159\u0163\u0003\u00d6k\u0000\u015a\u0163\u0003\u00d8" +
                    "l\u0000\u015b\u0163\u0003\u00dam\u0000\u015c\u0163\u0003\u00dcn\u0000" +
                    "\u015d\u0163\u0003\u00deo\u0000\u015e\u0163\u0003\u00e0p\u0000\u015f\u0163" +
                    "\u0003\u00e2q\u0000\u0160\u0163\u0003\u00e4r\u0000\u0161\u0163\u0003\u00e6" +
                    "s\u0000\u0162\u00f3\u0001\u0000\u0000\u0000\u0162\u00f4\u0001\u0000\u0000" +
                    "\u0000\u0162\u00f5\u0001\u0000\u0000\u0000\u0162\u00f6\u0001\u0000\u0000" +
                    "\u0000\u0162\u00f7\u0001\u0000\u0000\u0000\u0162\u00f8\u0001\u0000\u0000" +
                    "\u0000\u0162\u00f9\u0001\u0000\u0000\u0000\u0162\u00fa\u0001\u0000\u0000" +
                    "\u0000\u0162\u00fb\u0001\u0000\u0000\u0000\u0162\u00fc\u0001\u0000\u0000" +
                    "\u0000\u0162\u00fd\u0001\u0000\u0000\u0000\u0162\u00fe\u0001\u0000\u0000" +
                    "\u0000\u0162\u00ff\u0001\u0000\u0000\u0000\u0162\u0100\u0001\u0000\u0000" +
                    "\u0000\u0162\u0101\u0001\u0000\u0000\u0000\u0162\u0102\u0001\u0000\u0000" +
                    "\u0000\u0162\u0103\u0001\u0000\u0000\u0000\u0162\u0104\u0001\u0000\u0000" +
                    "\u0000\u0162\u0105\u0001\u0000\u0000\u0000\u0162\u0106\u0001\u0000\u0000" +
                    "\u0000\u0162\u0107\u0001\u0000\u0000\u0000\u0162\u0108\u0001\u0000\u0000" +
                    "\u0000\u0162\u0109\u0001\u0000\u0000\u0000\u0162\u010a\u0001\u0000\u0000" +
                    "\u0000\u0162\u010b\u0001\u0000\u0000\u0000\u0162\u010c\u0001\u0000\u0000" +
                    "\u0000\u0162\u010d\u0001\u0000\u0000\u0000\u0162\u010e\u0001\u0000\u0000" +
                    "\u0000\u0162\u010f\u0001\u0000\u0000\u0000\u0162\u0110\u0001\u0000\u0000" +
                    "\u0000\u0162\u0111\u0001\u0000\u0000\u0000\u0162\u0112\u0001\u0000\u0000" +
                    "\u0000\u0162\u0113\u0001\u0000\u0000\u0000\u0162\u0114\u0001\u0000\u0000" +
                    "\u0000\u0162\u0115\u0001\u0000\u0000\u0000\u0162\u0116\u0001\u0000\u0000" +
                    "\u0000\u0162\u0117\u0001\u0000\u0000\u0000\u0162\u0118\u0001\u0000\u0000" +
                    "\u0000\u0162\u0119\u0001\u0000\u0000\u0000\u0162\u011a\u0001\u0000\u0000" +
                    "\u0000\u0162\u011b\u0001\u0000\u0000\u0000\u0162\u011c\u0001\u0000\u0000" +
                    "\u0000\u0162\u011d\u0001\u0000\u0000\u0000\u0162\u011e\u0001\u0000\u0000" +
                    "\u0000\u0162\u011f\u0001\u0000\u0000\u0000\u0162\u0120\u0001\u0000\u0000" +
                    "\u0000\u0162\u0121\u0001\u0000\u0000\u0000\u0162\u0122\u0001\u0000\u0000" +
                    "\u0000\u0162\u0123\u0001\u0000\u0000\u0000\u0162\u0124\u0001\u0000\u0000" +
                    "\u0000\u0162\u0125\u0001\u0000\u0000\u0000\u0162\u0126\u0001\u0000\u0000" +
                    "\u0000\u0162\u0127\u0001\u0000\u0000\u0000\u0162\u0128\u0001\u0000\u0000" +
                    "\u0000\u0162\u0129\u0001\u0000\u0000\u0000\u0162\u012a\u0001\u0000\u0000" +
                    "\u0000\u0162\u012b\u0001\u0000\u0000\u0000\u0162\u012c\u0001\u0000\u0000" +
                    "\u0000\u0162\u012d\u0001\u0000\u0000\u0000\u0162\u012e\u0001\u0000\u0000" +
                    "\u0000\u0162\u012f\u0001\u0000\u0000\u0000\u0162\u0130\u0001\u0000\u0000" +
                    "\u0000\u0162\u0131\u0001\u0000\u0000\u0000\u0162\u0132\u0001\u0000\u0000" +
                    "\u0000\u0162\u0133\u0001\u0000\u0000\u0000\u0162\u0134\u0001\u0000\u0000" +
                    "\u0000\u0162\u0135\u0001\u0000\u0000\u0000\u0162\u0136\u0001\u0000\u0000" +
                    "\u0000\u0162\u0137\u0001\u0000\u0000\u0000\u0162\u0138\u0001\u0000\u0000" +
                    "\u0000\u0162\u0139\u0001\u0000\u0000\u0000\u0162\u013a\u0001\u0000\u0000" +
                    "\u0000\u0162\u013b\u0001\u0000\u0000\u0000\u0162\u013c\u0001\u0000\u0000" +
                    "\u0000\u0162\u013d\u0001\u0000\u0000\u0000\u0162\u013e\u0001\u0000\u0000" +
                    "\u0000\u0162\u013f\u0001\u0000\u0000\u0000\u0162\u0140\u0001\u0000\u0000" +
                    "\u0000\u0162\u0141\u0001\u0000\u0000\u0000\u0162\u0142\u0001\u0000\u0000" +
                    "\u0000\u0162\u0143\u0001\u0000\u0000\u0000\u0162\u0144\u0001\u0000\u0000" +
                    "\u0000\u0162\u0145\u0001\u0000\u0000\u0000\u0162\u0146\u0001\u0000\u0000" +
                    "\u0000\u0162\u0147\u0001\u0000\u0000\u0000\u0162\u0148\u0001\u0000\u0000" +
                    "\u0000\u0162\u0149\u0001\u0000\u0000\u0000\u0162\u014a\u0001\u0000\u0000" +
                    "\u0000\u0162\u014b\u0001\u0000\u0000\u0000\u0162\u014c\u0001\u0000\u0000" +
                    "\u0000\u0162\u014d\u0001\u0000\u0000\u0000\u0162\u014e\u0001\u0000\u0000" +
                    "\u0000\u0162\u014f\u0001\u0000\u0000\u0000\u0162\u0150\u0001\u0000\u0000" +
                    "\u0000\u0162\u0151\u0001\u0000\u0000\u0000\u0162\u0152\u0001\u0000\u0000" +
                    "\u0000\u0162\u0153\u0001\u0000\u0000\u0000\u0162\u0154\u0001\u0000\u0000" +
                    "\u0000\u0162\u0155\u0001\u0000\u0000\u0000\u0162\u0156\u0001\u0000\u0000" +
                    "\u0000\u0162\u0157\u0001\u0000\u0000\u0000\u0162\u0158\u0001\u0000\u0000" +
                    "\u0000\u0162\u0159\u0001\u0000\u0000\u0000\u0162\u015a\u0001\u0000\u0000" +
                    "\u0000\u0162\u015b\u0001\u0000\u0000\u0000\u0162\u015c\u0001\u0000\u0000" +
                    "\u0000\u0162\u015d\u0001\u0000\u0000\u0000\u0162\u015e\u0001\u0000\u0000" +
                    "\u0000\u0162\u015f\u0001\u0000\u0000\u0000\u0162\u0160\u0001\u0000\u0000" +
                    "\u0000\u0162\u0161\u0001\u0000\u0000\u0000\u0163\u0003\u0001\u0000\u0000" +
                    "\u0000\u0164\u0165\u0005\u0001\u0000\u0000\u0165\u0166\u0007\u0000\u0000" +
                    "\u0000\u0166\u0005\u0001\u0000\u0000\u0000\u0167\u0169\u0005\u0082\u0000" +
                    "\u0000\u0168\u0167\u0001\u0000\u0000\u0000\u0168\u0169\u0001\u0000\u0000" +
                    "\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u016b\u0005\u0004\u0000" +
                    "\u0000\u016b\u0007\u0001\u0000\u0000\u0000\u016c\u016e\u0005\u0082\u0000" +
                    "\u0000\u016d\u016c\u0001\u0000\u0000\u0000\u016d\u016e\u0001\u0000\u0000" +
                    "\u0000\u016e\u016f\u0001\u0000\u0000\u0000\u016f\u0170\u0005\u0005\u0000" +
                    "\u0000\u0170\t\u0001\u0000\u0000\u0000\u0171\u0173\u0005\u0082\u0000\u0000" +
                    "\u0172\u0171\u0001\u0000\u0000\u0000\u0172\u0173\u0001\u0000\u0000\u0000" +
                    "\u0173\u0174\u0001\u0000\u0000\u0000\u0174\u0175\u0005\u0006\u0000\u0000" +
                    "\u0175\u0179\u0005\u0088\u0000\u0000\u0176\u0178\u0005\u0088\u0000\u0000" +
                    "\u0177\u0176\u0001\u0000\u0000\u0000\u0178\u017b\u0001\u0000\u0000\u0000" +
                    "\u0179\u0177\u0001\u0000\u0000\u0000\u0179\u017a\u0001\u0000\u0000\u0000" +
                    "\u017a\u000b\u0001\u0000\u0000\u0000\u017b\u0179\u0001\u0000\u0000\u0000" +
                    "\u017c\u017e\u0005\u0082\u0000\u0000\u017d\u017c\u0001\u0000\u0000\u0000" +
                    "\u017d\u017e\u0001\u0000\u0000\u0000\u017e\u017f\u0001\u0000\u0000\u0000" +
                    "\u017f\u0180\u0005\u0007\u0000\u0000\u0180\u0184\u0005\u0087\u0000\u0000" +
                    "\u0181\u0183\u0003\u0002\u0001\u0000\u0182\u0181\u0001\u0000\u0000\u0000" +
                    "\u0183\u0186\u0001\u0000\u0000\u0000\u0184\u0182\u0001\u0000\u0000\u0000" +
                    "\u0184\u0185\u0001\u0000\u0000\u0000\u0185\u0187\u0001\u0000\u0000\u0000" +
                    "\u0186\u0184\u0001\u0000\u0000\u0000\u0187\u0188\u0003T*\u0000\u0188\r" +
                    "\u0001\u0000\u0000\u0000\u0189\u018b\u0005\u0082\u0000\u0000\u018a\u0189" +
                    "\u0001\u0000\u0000\u0000\u018a\u018b\u0001\u0000\u0000\u0000\u018b\u018c" +
                    "\u0001\u0000\u0000\u0000\u018c\u018d\u0005\b\u0000\u0000\u018d\u018e\u0005" +
                    "\u0087\u0000\u0000\u018e\u018f\u0003V+\u0000\u018f\u000f\u0001\u0000\u0000" +
                    "\u0000\u0190\u0192\u0005\u0082\u0000\u0000\u0191\u0190\u0001\u0000\u0000" +
                    "\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192\u0193\u0001\u0000\u0000" +
                    "\u0000\u0193\u0194\u0005\t\u0000\u0000\u0194\u0198\u0005\u0087\u0000\u0000" +
                    "\u0195\u0197\u0003\u0002\u0001\u0000\u0196\u0195\u0001\u0000\u0000\u0000" +
                    "\u0197\u019a\u0001\u0000\u0000\u0000\u0198\u0196\u0001\u0000\u0000\u0000" +
                    "\u0198\u0199\u0001\u0000\u0000\u0000\u0199\u019b\u0001\u0000\u0000\u0000" +
                    "\u019a\u0198\u0001\u0000\u0000\u0000\u019b\u019c\u0003T*\u0000\u019c\u0011" +
                    "\u0001\u0000\u0000\u0000\u019d\u019f\u0005\u0082\u0000\u0000\u019e\u019d" +
                    "\u0001\u0000\u0000\u0000\u019e\u019f\u0001\u0000\u0000\u0000\u019f\u01a0" +
                    "\u0001\u0000\u0000\u0000\u01a0\u01a1\u0005\n\u0000\u0000\u01a1\u01a2\u0005" +
                    "\u0087\u0000\u0000\u01a2\u01a3\u0003V+\u0000\u01a3\u0013\u0001\u0000\u0000" +
                    "\u0000\u01a4\u01a6\u0005\u0082\u0000\u0000\u01a5\u01a4\u0001\u0000\u0000" +
                    "\u0000\u01a5\u01a6\u0001\u0000\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000" +
                    "\u0000\u01a7\u01a8\u0005\u000b\u0000\u0000\u01a8\u01ac\u0005\u0087\u0000" +
                    "\u0000\u01a9\u01ab\u0003\u0002\u0001\u0000\u01aa\u01a9\u0001\u0000\u0000" +
                    "\u0000\u01ab\u01ae\u0001\u0000\u0000\u0000\u01ac\u01aa\u0001\u0000\u0000" +
                    "\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000\u01ad\u01af\u0001\u0000\u0000" +
                    "\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01af\u01b0\u0003T*\u0000\u01b0" +
                    "\u0015\u0001\u0000\u0000\u0000\u01b1\u01b3\u0005\u0082\u0000\u0000\u01b2" +
                    "\u01b1\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3" +
                    "\u01b4\u0001\u0000\u0000\u0000\u01b4\u01b5\u0005\f\u0000\u0000\u01b5\u01b6" +
                    "\u0005\u0087\u0000\u0000\u01b6\u01b7\u0003V+\u0000\u01b7\u0017\u0001\u0000" +
                    "\u0000\u0000\u01b8\u01ba\u0005\u0082\u0000\u0000\u01b9\u01b8\u0001\u0000" +
                    "\u0000\u0000\u01b9\u01ba\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000" +
                    "\u0000\u0000\u01bb\u01bf\u0005\r\u0000\u0000\u01bc\u01be\u0003\u0002\u0001" +
                    "\u0000\u01bd\u01bc\u0001\u0000\u0000\u0000\u01be\u01c1\u0001\u0000\u0000" +
                    "\u0000\u01bf\u01bd\u0001\u0000\u0000\u0000\u01bf\u01c0\u0001\u0000\u0000" +
                    "\u0000\u01c0\u01c2\u0001\u0000\u0000\u0000\u01c1\u01bf\u0001\u0000\u0000" +
                    "\u0000\u01c2\u01c3\u0003T*\u0000\u01c3\u0019\u0001\u0000\u0000\u0000\u01c4" +
                    "\u01c6\u0005\u0082\u0000\u0000\u01c5\u01c4\u0001\u0000\u0000\u0000\u01c5" +
                    "\u01c6\u0001\u0000\u0000\u0000\u01c6\u01c7\u0001\u0000\u0000\u0000\u01c7" +
                    "\u01c8\u0005\u000e\u0000\u0000\u01c8\u01c9\u0003V+\u0000\u01c9\u001b\u0001" +
                    "\u0000\u0000\u0000\u01ca\u01cc\u0005\u0082\u0000\u0000\u01cb\u01ca\u0001" +
                    "\u0000\u0000\u0000\u01cb\u01cc\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001" +
                    "\u0000\u0000\u0000\u01cd\u01ce\u0005\u000f\u0000\u0000\u01ce\u01d2\u0005" +
                    "\u0087\u0000\u0000\u01cf\u01d1\u0003\u0002\u0001\u0000\u01d0\u01cf\u0001" +
                    "\u0000\u0000\u0000\u01d1\u01d4\u0001\u0000\u0000\u0000\u01d2\u01d0\u0001" +
                    "\u0000\u0000\u0000\u01d2\u01d3\u0001\u0000\u0000\u0000\u01d3\u01d5\u0001" +
                    "\u0000\u0000\u0000\u01d4\u01d2\u0001\u0000\u0000\u0000\u01d5\u01d6\u0003" +
                    "T*\u0000\u01d6\u001d\u0001\u0000\u0000\u0000\u01d7\u01d9\u0005\u0082\u0000" +
                    "\u0000\u01d8\u01d7\u0001\u0000\u0000\u0000\u01d8\u01d9\u0001\u0000\u0000" +
                    "\u0000\u01d9\u01da\u0001\u0000\u0000\u0000\u01da\u01db\u0005\u0010\u0000" +
                    "\u0000\u01db\u01dc\u0005\u0087\u0000\u0000\u01dc\u01dd\u0003V+\u0000\u01dd" +
                    "\u001f\u0001\u0000\u0000\u0000\u01de\u01e0\u0005\u0082\u0000\u0000\u01df" +
                    "\u01de\u0001\u0000\u0000\u0000\u01df\u01e0\u0001\u0000\u0000\u0000\u01e0" +
                    "\u01e1\u0001\u0000\u0000\u0000\u01e1\u01e2\u0005\u0011\u0000\u0000\u01e2" +
                    "\u01e6\u0005\u0087\u0000\u0000\u01e3\u01e5\u0003\u0002\u0001\u0000\u01e4" +
                    "\u01e3\u0001\u0000\u0000\u0000\u01e5\u01e8\u0001\u0000\u0000\u0000\u01e6" +
                    "\u01e4\u0001\u0000\u0000\u0000\u01e6\u01e7\u0001\u0000\u0000\u0000\u01e7" +
                    "\u01e9\u0001\u0000\u0000\u0000\u01e8\u01e6\u0001\u0000\u0000\u0000\u01e9" +
                    "\u01ea\u0003T*\u0000\u01ea!\u0001\u0000\u0000\u0000\u01eb\u01ed\u0005" +
                    "\u0082\u0000\u0000\u01ec\u01eb\u0001\u0000\u0000\u0000\u01ec\u01ed\u0001" +
                    "\u0000\u0000\u0000\u01ed\u01ee\u0001\u0000\u0000\u0000\u01ee\u01ef\u0005" +
                    "\u0012\u0000\u0000\u01ef\u01f0\u0005\u0087\u0000\u0000\u01f0\u01f1\u0003" +
                    "V+\u0000\u01f1#\u0001\u0000\u0000\u0000\u01f2\u01f4\u0005\u0082\u0000" +
                    "\u0000\u01f3\u01f2\u0001\u0000\u0000\u0000\u01f3\u01f4\u0001\u0000\u0000" +
                    "\u0000\u01f4\u01f5\u0001\u0000\u0000\u0000\u01f5\u01f9\u0005\u0013\u0000" +
                    "\u0000\u01f6\u01f8\u0003\u0002\u0001\u0000\u01f7\u01f6\u0001\u0000\u0000" +
                    "\u0000\u01f8\u01fb\u0001\u0000\u0000\u0000\u01f9\u01f7\u0001\u0000\u0000" +
                    "\u0000\u01f9\u01fa\u0001\u0000\u0000\u0000\u01fa\u01fc\u0001\u0000\u0000" +
                    "\u0000\u01fb\u01f9\u0001\u0000\u0000\u0000\u01fc\u01fd\u0003T*\u0000\u01fd" +
                    "%\u0001\u0000\u0000\u0000\u01fe\u0200\u0005\u0082\u0000\u0000\u01ff\u01fe" +
                    "\u0001\u0000\u0000\u0000\u01ff\u0200\u0001\u0000\u0000\u0000\u0200\u0201" +
                    "\u0001\u0000\u0000\u0000\u0201\u0202\u0005\u0014\u0000\u0000\u0202\u0203" +
                    "\u0003V+\u0000\u0203\'\u0001\u0000\u0000\u0000\u0204\u0206\u0005\u0082" +
                    "\u0000\u0000\u0205\u0204\u0001\u0000\u0000\u0000\u0205\u0206\u0001\u0000" +
                    "\u0000\u0000\u0206\u0207\u0001\u0000\u0000\u0000\u0207\u0208\u0005\u0015" +
                    "\u0000\u0000\u0208\u020c\u0005\u0087\u0000\u0000\u0209\u020b\u0003\u0002" +
                    "\u0001\u0000\u020a\u0209\u0001\u0000\u0000\u0000\u020b\u020e\u0001\u0000" +
                    "\u0000\u0000\u020c\u020a\u0001\u0000\u0000\u0000\u020c\u020d\u0001\u0000" +
                    "\u0000\u0000\u020d\u020f\u0001\u0000\u0000\u0000\u020e\u020c\u0001\u0000" +
                    "\u0000\u0000\u020f\u0210\u0003T*\u0000\u0210)\u0001\u0000\u0000\u0000" +
                    "\u0211\u0213\u0005\u0082\u0000\u0000\u0212\u0211\u0001\u0000\u0000\u0000" +
                    "\u0212\u0213\u0001\u0000\u0000\u0000\u0213\u0214\u0001\u0000\u0000\u0000" +
                    "\u0214\u0215\u0005\u0016\u0000\u0000\u0215\u0216\u0005\u0087\u0000\u0000" +
                    "\u0216\u0217\u0003V+\u0000\u0217+\u0001\u0000\u0000\u0000\u0218\u021a" +
                    "\u0005\u0082\u0000\u0000\u0219\u0218\u0001\u0000\u0000\u0000\u0219\u021a" +
                    "\u0001\u0000\u0000\u0000\u021a\u021b\u0001\u0000\u0000\u0000\u021b\u021c" +
                    "\u0005\u0017\u0000\u0000\u021c\u0220\u0005\u0081\u0000\u0000\u021d\u021f" +
                    "\u0003\u0002\u0001\u0000\u021e\u021d\u0001\u0000\u0000\u0000\u021f\u0222" +
                    "\u0001\u0000\u0000\u0000\u0220\u021e\u0001\u0000\u0000\u0000\u0220\u0221" +
                    "\u0001\u0000\u0000\u0000\u0221\u0223\u0001\u0000\u0000\u0000\u0222\u0220" +
                    "\u0001\u0000\u0000\u0000\u0223\u0224\u0003T*\u0000\u0224-\u0001\u0000" +
                    "\u0000\u0000\u0225\u0227\u0005\u0082\u0000\u0000\u0226\u0225\u0001\u0000" +
                    "\u0000\u0000\u0226\u0227\u0001\u0000\u0000\u0000\u0227\u0228\u0001\u0000" +
                    "\u0000\u0000\u0228\u0229\u0005\u0018\u0000\u0000\u0229\u022a\u0005\u0081" +
                    "\u0000\u0000\u022a\u022b\u0003V+\u0000\u022b/\u0001\u0000\u0000\u0000" +
                    "\u022c\u022e\u0005\u0082\u0000\u0000\u022d\u022c\u0001\u0000\u0000\u0000" +
                    "\u022d\u022e\u0001\u0000\u0000\u0000\u022e\u022f\u0001\u0000\u0000\u0000" +
                    "\u022f\u0230\u0005\u0019\u0000\u0000\u0230\u0234\u0005\u008a\u0000\u0000" +
                    "\u0231\u0233\u0003\u0002\u0001\u0000\u0232\u0231\u0001\u0000\u0000\u0000" +
                    "\u0233\u0236\u0001\u0000\u0000\u0000\u0234\u0232\u0001\u0000\u0000\u0000" +
                    "\u0234\u0235\u0001\u0000\u0000\u0000\u0235\u0237\u0001\u0000\u0000\u0000" +
                    "\u0236\u0234\u0001\u0000\u0000\u0000\u0237\u0238\u0003T*\u0000\u02381" +
                    "\u0001\u0000\u0000\u0000\u0239\u023b\u0005\u0082\u0000\u0000\u023a\u0239" +
                    "\u0001\u0000\u0000\u0000\u023a\u023b\u0001\u0000\u0000\u0000\u023b\u023c" +
                    "\u0001\u0000\u0000\u0000\u023c\u023d\u0005\u001a\u0000\u0000\u023d\u023e" +
                    "\u0005\u008a\u0000\u0000\u023e\u023f\u0003V+\u0000\u023f3\u0001\u0000" +
                    "\u0000\u0000\u0240\u0242\u0005\u0082\u0000\u0000\u0241\u0240\u0001\u0000" +
                    "\u0000\u0000\u0241\u0242\u0001\u0000\u0000\u0000\u0242\u0243\u0001\u0000" +
                    "\u0000\u0000\u0243\u0244\u0005\u001b\u0000\u0000\u0244\u0245\u0005\u0088" +
                    "\u0000\u0000\u0245\u0249\u0005\u0088\u0000\u0000\u0246\u0248\u0003\u0002" +
                    "\u0001\u0000\u0247\u0246\u0001\u0000\u0000\u0000\u0248\u024b\u0001\u0000" +
                    "\u0000\u0000\u0249\u0247\u0001\u0000\u0000\u0000\u0249\u024a\u0001\u0000" +
                    "\u0000\u0000\u024a\u024c\u0001\u0000\u0000\u0000\u024b\u0249\u0001\u0000" +
                    "\u0000\u0000\u024c\u024d\u0003T*\u0000\u024d5\u0001\u0000\u0000\u0000" +
                    "\u024e\u0250\u0005\u0082\u0000\u0000\u024f\u024e\u0001\u0000\u0000\u0000" +
                    "\u024f\u0250\u0001\u0000\u0000\u0000\u0250\u0251\u0001\u0000\u0000\u0000" +
                    "\u0251\u0252\u0005\u001c\u0000\u0000\u0252\u0253\u0005\u0088\u0000\u0000" +
                    "\u0253\u0254\u0005\u0088\u0000\u0000\u0254\u0255\u0003V+\u0000\u02557" +
                    "\u0001\u0000\u0000\u0000\u0256\u0258\u0005\u0082\u0000\u0000\u0257\u0256" +
                    "\u0001\u0000\u0000\u0000\u0257\u0258\u0001\u0000\u0000\u0000\u0258\u0259" +
                    "\u0001\u0000\u0000\u0000\u0259\u025a\u0005\u001d\u0000\u0000\u025a\u025b" +
                    "\u0005\u0088\u0000\u0000\u025b\u025c\u0005\u0088\u0000\u0000\u025c\u025d" +
                    "\u0005\u0088\u0000\u0000\u025d\u0261\u0005\u0088\u0000\u0000\u025e\u0260" +
                    "\u0003\u0002\u0001\u0000\u025f\u025e\u0001\u0000\u0000\u0000\u0260\u0263" +
                    "\u0001\u0000\u0000\u0000\u0261\u025f\u0001\u0000\u0000\u0000\u0261\u0262" +
                    "\u0001\u0000\u0000\u0000\u0262\u0264\u0001\u0000\u0000\u0000\u0263\u0261" +
                    "\u0001\u0000\u0000\u0000\u0264\u0265\u0003T*\u0000\u02659\u0001\u0000" +
                    "\u0000\u0000\u0266\u0268\u0005\u0082\u0000\u0000\u0267\u0266\u0001\u0000" +
                    "\u0000\u0000\u0267\u0268\u0001\u0000\u0000\u0000\u0268\u0269\u0001\u0000" +
                    "\u0000\u0000\u0269\u026a\u0005\u001e\u0000\u0000\u026a\u026b\u0005\u0088" +
                    "\u0000\u0000\u026b\u026c\u0005\u0088\u0000\u0000\u026c\u026d\u0005\u0088" +
                    "\u0000\u0000\u026d\u026e\u0005\u0088\u0000\u0000\u026e\u026f\u0003V+\u0000" +
                    "\u026f;\u0001\u0000\u0000\u0000\u0270\u0272\u0005\u0082\u0000\u0000\u0271" +
                    "\u0270\u0001\u0000\u0000\u0000\u0271\u0272\u0001\u0000\u0000\u0000\u0272" +
                    "\u0273\u0001\u0000\u0000\u0000\u0273\u0274\u0005\u001f\u0000\u0000\u0274" +
                    "\u0278\u0005\u008a\u0000\u0000\u0275\u0277\u0003\u0002\u0001\u0000\u0276" +
                    "\u0275\u0001\u0000\u0000\u0000\u0277\u027a\u0001\u0000\u0000\u0000\u0278" +
                    "\u0276\u0001\u0000\u0000\u0000\u0278\u0279\u0001\u0000\u0000\u0000\u0279" +
                    "\u027b\u0001\u0000\u0000\u0000\u027a\u0278\u0001\u0000\u0000\u0000\u027b" +
                    "\u027c\u0003T*\u0000\u027c=\u0001\u0000\u0000\u0000\u027d\u027f\u0005" +
                    "\u0082\u0000\u0000\u027e\u027d\u0001\u0000\u0000\u0000\u027e\u027f\u0001" +
                    "\u0000\u0000\u0000\u027f\u0280\u0001\u0000\u0000\u0000\u0280\u0281\u0005" +
                    " \u0000\u0000\u0281\u0282\u0005\u008a\u0000\u0000\u0282\u0283\u0003V+" +
                    "\u0000\u0283?\u0001\u0000\u0000\u0000\u0284\u0286\u0005\u0082\u0000\u0000" +
                    "\u0285\u0284\u0001\u0000\u0000\u0000\u0285\u0286\u0001\u0000\u0000\u0000" +
                    "\u0286\u0287\u0001\u0000\u0000\u0000\u0287\u0288\u0005!\u0000\u0000\u0288" +
                    "\u028c\u0005\u0088\u0000\u0000\u0289\u028b\u0003\u0002\u0001\u0000\u028a" +
                    "\u0289\u0001\u0000\u0000\u0000\u028b\u028e\u0001\u0000\u0000\u0000\u028c" +
                    "\u028a\u0001\u0000\u0000\u0000\u028c\u028d\u0001\u0000\u0000\u0000\u028d" +
                    "\u028f\u0001\u0000\u0000\u0000\u028e\u028c\u0001\u0000\u0000\u0000\u028f" +
                    "\u0290\u0003T*\u0000\u0290A\u0001\u0000\u0000\u0000\u0291\u0293\u0005" +
                    "\u0082\u0000\u0000\u0292\u0291\u0001\u0000\u0000\u0000\u0292\u0293\u0001" +
                    "\u0000\u0000\u0000\u0293\u0294\u0001\u0000\u0000\u0000\u0294\u0295\u0005" +
                    "\"\u0000\u0000\u0295\u0296\u0005\u0088\u0000\u0000\u0296\u0297\u0003V" +
                    "+\u0000\u0297C\u0001\u0000\u0000\u0000\u0298\u029a\u0005\u0082\u0000\u0000" +
                    "\u0299\u0298\u0001\u0000\u0000\u0000\u0299\u029a\u0001\u0000\u0000\u0000" +
                    "\u029a\u029b\u0001\u0000\u0000\u0000\u029b\u029c\u0005#\u0000\u0000\u029c" +
                    "\u02a0\u0005\u008a\u0000\u0000\u029d\u029f\u0003\u0002\u0001\u0000\u029e" +
                    "\u029d\u0001\u0000\u0000\u0000\u029f\u02a2\u0001\u0000\u0000\u0000\u02a0" +
                    "\u029e\u0001\u0000\u0000\u0000\u02a0\u02a1\u0001\u0000\u0000\u0000\u02a1" +
                    "\u02a3\u0001\u0000\u0000\u0000\u02a2\u02a0\u0001\u0000\u0000\u0000\u02a3" +
                    "\u02a4\u0003T*\u0000\u02a4E\u0001\u0000\u0000\u0000\u02a5\u02a7\u0005" +
                    "\u0082\u0000\u0000\u02a6\u02a5\u0001\u0000\u0000\u0000\u02a6\u02a7\u0001" +
                    "\u0000\u0000\u0000\u02a7\u02a8\u0001\u0000\u0000\u0000\u02a8\u02a9\u0005" +
                    "$\u0000\u0000\u02a9\u02aa\u0005\u008a\u0000\u0000\u02aa\u02ab\u0003V+" +
                    "\u0000\u02abG\u0001\u0000\u0000\u0000\u02ac\u02ae\u0005\u0082\u0000\u0000" +
                    "\u02ad\u02ac\u0001\u0000\u0000\u0000\u02ad\u02ae\u0001\u0000\u0000\u0000" +
                    "\u02ae\u02af\u0001\u0000\u0000\u0000\u02af\u02b3\u0005%\u0000\u0000\u02b0" +
                    "\u02b2\u0003\u0002\u0001\u0000\u02b1\u02b0\u0001\u0000\u0000\u0000\u02b2" +
                    "\u02b5\u0001\u0000\u0000\u0000\u02b3\u02b1\u0001\u0000\u0000\u0000\u02b3" +
                    "\u02b4\u0001\u0000\u0000\u0000\u02b4\u02b6\u0001\u0000\u0000\u0000\u02b5" +
                    "\u02b3\u0001\u0000\u0000\u0000\u02b6\u02b7\u0003T*\u0000\u02b7I\u0001" +
                    "\u0000\u0000\u0000\u02b8\u02ba\u0005\u0082\u0000\u0000\u02b9\u02b8\u0001" +
                    "\u0000\u0000\u0000\u02b9\u02ba\u0001\u0000\u0000\u0000\u02ba\u02bb\u0001" +
                    "\u0000\u0000\u0000\u02bb\u02bc\u0005&\u0000\u0000\u02bc\u02bd\u0003V+" +
                    "\u0000\u02bdK\u0001\u0000\u0000\u0000\u02be\u02c0\u0005\u0082\u0000\u0000" +
                    "\u02bf\u02be\u0001\u0000\u0000\u0000\u02bf\u02c0\u0001\u0000\u0000\u0000" +
                    "\u02c0\u02c1\u0001\u0000\u0000\u0000\u02c1\u02c2\u0005\'\u0000\u0000\u02c2" +
                    "\u02c6\u0005\u008a\u0000\u0000\u02c3\u02c5\u0003\u0002\u0001\u0000\u02c4" +
                    "\u02c3\u0001\u0000\u0000\u0000\u02c5\u02c8\u0001\u0000\u0000\u0000\u02c6" +
                    "\u02c4\u0001\u0000\u0000\u0000\u02c6\u02c7\u0001\u0000\u0000\u0000\u02c7" +
                    "\u02c9\u0001\u0000\u0000\u0000\u02c8\u02c6\u0001\u0000\u0000\u0000\u02c9" +
                    "\u02ca\u0003T*\u0000\u02caM\u0001\u0000\u0000\u0000\u02cb\u02cd\u0005" +
                    "\u0082\u0000\u0000\u02cc\u02cb\u0001\u0000\u0000\u0000\u02cc\u02cd\u0001" +
                    "\u0000\u0000\u0000\u02cd\u02ce\u0001\u0000\u0000\u0000\u02ce\u02cf\u0005" +
                    "(\u0000\u0000\u02cf\u02d0\u0005\u008a\u0000\u0000\u02d0\u02d1\u0003V+" +
                    "\u0000\u02d1O\u0001\u0000\u0000\u0000\u02d2\u02d4\u0005\u0082\u0000\u0000" +
                    "\u02d3\u02d2\u0001\u0000\u0000\u0000\u02d3\u02d4\u0001\u0000\u0000\u0000" +
                    "\u02d4\u02d5\u0001\u0000\u0000\u0000\u02d5\u02d9\u0005)\u0000\u0000\u02d6" +
                    "\u02d8\u0003\u0002\u0001\u0000\u02d7\u02d6\u0001\u0000\u0000\u0000\u02d8" +
                    "\u02db\u0001\u0000\u0000\u0000\u02d9\u02d7\u0001\u0000\u0000\u0000\u02d9" +
                    "\u02da\u0001\u0000\u0000\u0000\u02da\u02dc\u0001\u0000\u0000\u0000\u02db" +
                    "\u02d9\u0001\u0000\u0000\u0000\u02dc\u02dd\u0003T*\u0000\u02ddQ\u0001" +
                    "\u0000\u0000\u0000\u02de\u02e0\u0005\u0082\u0000\u0000\u02df\u02de\u0001" +
                    "\u0000\u0000\u0000\u02df\u02e0\u0001\u0000\u0000\u0000\u02e0\u02e1\u0001" +
                    "\u0000\u0000\u0000\u02e1\u02e2\u0005*\u0000\u0000\u02e2\u02e3\u0003V+" +
                    "\u0000\u02e3S\u0001\u0000\u0000\u0000\u02e4\u02e5\u0007\u0001\u0000\u0000" +
                    "\u02e5U\u0001\u0000\u0000\u0000\u02e6\u02e7\u0005\u0086\u0000\u0000\u02e7" +
                    "W\u0001\u0000\u0000\u0000\u02e8\u02ea\u0005\u0082\u0000\u0000\u02e9\u02e8" +
                    "\u0001\u0000\u0000\u0000\u02e9\u02ea\u0001\u0000\u0000\u0000\u02ea\u02eb" +
                    "\u0001\u0000\u0000\u0000\u02eb\u02ef\u0005-\u0000\u0000\u02ec\u02ee\u0003" +
                    "\u0002\u0001\u0000\u02ed\u02ec\u0001\u0000\u0000\u0000\u02ee\u02f1\u0001" +
                    "\u0000\u0000\u0000\u02ef\u02ed\u0001\u0000\u0000\u0000\u02ef\u02f0\u0001" +
                    "\u0000\u0000\u0000\u02f0\u02f2\u0001\u0000\u0000\u0000\u02f1\u02ef\u0001" +
                    "\u0000\u0000\u0000\u02f2\u02f3\u0005.\u0000\u0000\u02f3\u02f4\u0005\u008a" +
                    "\u0000\u0000\u02f4Y\u0001\u0000\u0000\u0000\u02f5\u02f7\u0005\u0082\u0000" +
                    "\u0000\u02f6\u02f5\u0001\u0000\u0000\u0000\u02f6\u02f7\u0001\u0000\u0000" +
                    "\u0000\u02f7\u02f8\u0001\u0000\u0000\u0000\u02f8\u02fb\u0005/\u0000\u0000" +
                    "\u02f9\u02fc\u0007\u0002\u0000\u0000\u02fa\u02fc\u0005\u0088\u0000\u0000" +
                    "\u02fb\u02f9\u0001\u0000\u0000\u0000\u02fb\u02fa\u0001\u0000\u0000\u0000" +
                    "\u02fc[\u0001\u0000\u0000\u0000\u02fd\u02ff\u0005\u0082\u0000\u0000\u02fe" +
                    "\u02fd\u0001\u0000\u0000\u0000\u02fe\u02ff\u0001\u0000\u0000\u0000\u02ff" +
                    "\u0300\u0001\u0000\u0000\u0000\u0300\u0301\u00052\u0000\u0000\u0301\u0302" +
                    "\u0005\u0088\u0000\u0000\u0302]\u0001\u0000\u0000\u0000\u0303\u0305\u0005" +
                    "\u0082\u0000\u0000\u0304\u0303\u0001\u0000\u0000\u0000\u0304\u0305\u0001" +
                    "\u0000\u0000\u0000\u0305\u0306\u0001\u0000\u0000\u0000\u0306\u0307\u0005" +
                    "3\u0000\u0000\u0307_\u0001\u0000\u0000\u0000\u0308\u030a\u0005\u0082\u0000" +
                    "\u0000\u0309\u0308\u0001\u0000\u0000\u0000\u0309\u030a\u0001\u0000\u0000" +
                    "\u0000\u030a\u030b\u0001\u0000\u0000\u0000\u030b\u030c\u00054\u0000\u0000" +
                    "\u030c\u030d\u0005\u0088\u0000\u0000\u030da\u0001\u0000\u0000\u0000\u030e" +
                    "\u0310\u0005\u0082\u0000\u0000\u030f\u030e\u0001\u0000\u0000\u0000\u030f" +
                    "\u0310\u0001\u0000\u0000\u0000\u0310\u0311\u0001\u0000\u0000\u0000\u0311" +
                    "\u0312\u00055\u0000\u0000\u0312c\u0001\u0000\u0000\u0000\u0313\u0315\u0005" +
                    "\u0082\u0000\u0000\u0314\u0313\u0001\u0000\u0000\u0000\u0314\u0315\u0001" +
                    "\u0000\u0000\u0000\u0315\u0316\u0001\u0000\u0000\u0000\u0316\u0317\u0005" +
                    "6\u0000\u0000\u0317\u0318\u0005\u0088\u0000\u0000\u0318e\u0001\u0000\u0000" +
                    "\u0000\u0319\u031b\u0005\u0082\u0000\u0000\u031a\u0319\u0001\u0000\u0000" +
                    "\u0000\u031a\u031b\u0001\u0000\u0000\u0000\u031b\u031c\u0001\u0000\u0000" +
                    "\u0000\u031c\u031d\u00057\u0000\u0000\u031d\u031e\u0005\u0088\u0000\u0000" +
                    "\u031eg\u0001\u0000\u0000\u0000\u031f\u0321\u0005\u0082\u0000\u0000\u0320" +
                    "\u031f\u0001\u0000\u0000\u0000\u0320\u0321\u0001\u0000\u0000\u0000\u0321" +
                    "\u0322\u0001\u0000\u0000\u0000\u0322\u0323\u00058\u0000\u0000\u0323\u0324" +
                    "\u0005\u0088\u0000\u0000\u0324i\u0001\u0000\u0000\u0000\u0325\u0327\u0005" +
                    "\u0082\u0000\u0000\u0326\u0325\u0001\u0000\u0000\u0000\u0326\u0327\u0001" +
                    "\u0000\u0000\u0000\u0327\u0328\u0001\u0000\u0000\u0000\u0328\u0329\u0005" +
                    "9\u0000\u0000\u0329k\u0001\u0000\u0000\u0000\u032a\u032c\u0005\u0082\u0000" +
                    "\u0000\u032b\u032a\u0001\u0000\u0000\u0000\u032b\u032c\u0001\u0000\u0000" +
                    "\u0000\u032c\u032d\u0001\u0000\u0000\u0000\u032d\u032e\u0005:\u0000\u0000" +
                    "\u032em\u0001\u0000\u0000\u0000\u032f\u0331\u0005\u0082\u0000\u0000\u0330" +
                    "\u032f\u0001\u0000\u0000\u0000\u0330\u0331\u0001\u0000\u0000\u0000\u0331" +
                    "\u0332\u0001\u0000\u0000\u0000\u0332\u0333\u0005;\u0000\u0000\u0333o\u0001" +
                    "\u0000\u0000\u0000\u0334\u0336\u0005\u0082\u0000\u0000\u0335\u0334\u0001" +
                    "\u0000\u0000\u0000\u0335\u0336\u0001\u0000\u0000\u0000\u0336\u0337\u0001" +
                    "\u0000\u0000\u0000\u0337\u0338\u0005<\u0000\u0000\u0338\u0339\u0005\u0088" +
                    "\u0000\u0000\u0339q\u0001\u0000\u0000\u0000\u033a\u033c\u0005\u0082\u0000" +
                    "\u0000\u033b\u033a\u0001\u0000\u0000\u0000\u033b\u033c\u0001\u0000\u0000" +
                    "\u0000\u033c\u033d\u0001\u0000\u0000\u0000\u033d\u033e\u0005=\u0000\u0000" +
                    "\u033es\u0001\u0000\u0000\u0000\u033f\u0341\u0005\u0082\u0000\u0000\u0340" +
                    "\u033f\u0001\u0000\u0000\u0000\u0340\u0341\u0001\u0000\u0000\u0000\u0341" +
                    "\u0342\u0001\u0000\u0000\u0000\u0342\u0343\u0005>\u0000\u0000\u0343u\u0001" +
                    "\u0000\u0000\u0000\u0344\u0346\u0005\u0082\u0000\u0000\u0345\u0344\u0001" +
                    "\u0000\u0000\u0000\u0345\u0346\u0001\u0000\u0000\u0000\u0346\u0347\u0001" +
                    "\u0000\u0000\u0000\u0347\u0348\u0005?\u0000\u0000\u0348\u034a\u0005\u0081" +
                    "\u0000\u0000\u0349\u034b\u0005\u008a\u0000\u0000\u034a\u0349\u0001\u0000" +
                    "\u0000\u0000\u034a\u034b\u0001\u0000\u0000\u0000\u034bw\u0001\u0000\u0000" +
                    "\u0000\u034c\u034e\u0005\u0082\u0000\u0000\u034d\u034c\u0001\u0000\u0000" +
                    "\u0000\u034d\u034e\u0001\u0000\u0000\u0000\u034e\u034f\u0001\u0000\u0000" +
                    "\u0000\u034f\u0350\u0005@\u0000\u0000\u0350\u0351\u0005\u0088\u0000\u0000" +
                    "\u0351\u0352\u0005\u0088\u0000\u0000\u0352y\u0001\u0000\u0000\u0000\u0353" +
                    "\u0355\u0005\u0082\u0000\u0000\u0354\u0353\u0001\u0000\u0000\u0000\u0354" +
                    "\u0355\u0001\u0000\u0000\u0000\u0355\u0356\u0001\u0000\u0000\u0000\u0356" +
                    "\u0357\u0005A\u0000\u0000\u0357\u0358\u0005\u0088\u0000\u0000\u0358\u0359" +
                    "\u0005\u0088\u0000\u0000\u0359{\u0001\u0000\u0000\u0000\u035a\u035c\u0005" +
                    "\u0082\u0000\u0000\u035b\u035a\u0001\u0000\u0000\u0000\u035b\u035c\u0001" +
                    "\u0000\u0000\u0000\u035c\u035d\u0001\u0000\u0000\u0000\u035d\u035f\u0005" +
                    "B\u0000\u0000\u035e\u0360\u0005\u008a\u0000\u0000\u035f\u035e\u0001\u0000" +
                    "\u0000\u0000\u035f\u0360\u0001\u0000\u0000\u0000\u0360}\u0001\u0000\u0000" +
                    "\u0000\u0361\u0363\u0005\u0082\u0000\u0000\u0362\u0361\u0001\u0000\u0000" +
                    "\u0000\u0362\u0363\u0001\u0000\u0000\u0000\u0363\u0364\u0001\u0000\u0000" +
                    "\u0000\u0364\u0369\u0005C\u0000\u0000\u0365\u0366\u0005\u0088\u0000\u0000" +
                    "\u0366\u0367\u0005\u0088\u0000\u0000\u0367\u0368\u0005\u0088\u0000\u0000" +
                    "\u0368\u036a\u0005\u0088\u0000\u0000\u0369\u0365\u0001\u0000\u0000\u0000" +
                    "\u0369\u036a\u0001\u0000\u0000\u0000\u036a\u007f\u0001\u0000\u0000\u0000" +
                    "\u036b\u036d\u0005\u0082\u0000\u0000\u036c\u036b\u0001\u0000\u0000\u0000" +
                    "\u036c\u036d\u0001\u0000\u0000\u0000\u036d\u036e\u0001\u0000\u0000\u0000" +
                    "\u036e\u036f\u0005D\u0000\u0000\u036f\u0081\u0001\u0000\u0000\u0000\u0370" +
                    "\u0372\u0005\u0082\u0000\u0000\u0371\u0370\u0001\u0000\u0000\u0000\u0371" +
                    "\u0372\u0001\u0000\u0000\u0000\u0372\u0373\u0001\u0000\u0000\u0000\u0373" +
                    "\u0374\u0005E\u0000\u0000\u0374\u0375\u0005\u0088\u0000\u0000\u0375\u0083" +
                    "\u0001\u0000\u0000\u0000\u0376\u0378\u0005\u0082\u0000\u0000\u0377\u0376" +
                    "\u0001\u0000\u0000\u0000\u0377\u0378\u0001\u0000\u0000\u0000\u0378\u0379" +
                    "\u0001\u0000\u0000\u0000\u0379\u037a\u0005F\u0000\u0000\u037a\u037b\u0005" +
                    "\u0088\u0000\u0000\u037b\u0085\u0001\u0000\u0000\u0000\u037c\u037e\u0005" +
                    "\u0082\u0000\u0000\u037d\u037c\u0001\u0000\u0000\u0000\u037d\u037e\u0001" +
                    "\u0000\u0000\u0000\u037e\u037f\u0001\u0000\u0000\u0000\u037f\u0380\u0005" +
                    "G\u0000\u0000\u0380\u0381\u0005\u0088\u0000\u0000\u0381\u0087\u0001\u0000" +
                    "\u0000\u0000\u0382\u0384\u0005\u0082\u0000\u0000\u0383\u0382\u0001\u0000" +
                    "\u0000\u0000\u0383\u0384\u0001\u0000\u0000\u0000\u0384\u0385\u0001\u0000" +
                    "\u0000\u0000\u0385\u0386\u0005H\u0000\u0000\u0386\u0089\u0001\u0000\u0000" +
                    "\u0000\u0387\u0389\u0005\u0082\u0000\u0000\u0388\u0387\u0001\u0000\u0000" +
                    "\u0000\u0388\u0389\u0001\u0000\u0000\u0000\u0389\u038a\u0001\u0000\u0000" +
                    "\u0000\u038a\u038b\u0005I\u0000\u0000\u038b\u038c\u0005\u0088\u0000\u0000" +
                    "\u038c\u038d\u0005\u0088\u0000\u0000\u038d\u008b\u0001\u0000\u0000\u0000" +
                    "\u038e\u0390\u0005\u0082\u0000\u0000\u038f\u038e\u0001\u0000\u0000\u0000" +
                    "\u038f\u0390\u0001\u0000\u0000\u0000\u0390\u0391\u0001\u0000\u0000\u0000" +
                    "\u0391\u0392\u0005J\u0000\u0000\u0392\u0393\u0005\u008a\u0000\u0000\u0393" +
                    "\u008d\u0001\u0000\u0000\u0000\u0394\u0396\u0005\u0082\u0000\u0000\u0395" +
                    "\u0394\u0001\u0000\u0000\u0000\u0395\u0396\u0001\u0000\u0000\u0000\u0396" +
                    "\u0397\u0001\u0000\u0000\u0000\u0397\u0398\u0005K\u0000\u0000\u0398\u0399" +
                    "\u0005\u0088\u0000\u0000\u0399\u039a\u0005\u0088\u0000\u0000\u039a\u008f" +
                    "\u0001\u0000\u0000\u0000\u039b\u039d\u0005\u0082\u0000\u0000\u039c\u039b" +
                    "\u0001\u0000\u0000\u0000\u039c\u039d\u0001\u0000\u0000\u0000\u039d\u039e" +
                    "\u0001\u0000\u0000\u0000\u039e\u039f\u0005L\u0000\u0000\u039f\u03a0\u0005" +
                    "\u0088\u0000\u0000\u03a0\u0091\u0001\u0000\u0000\u0000\u03a1\u03a3\u0005" +
                    "\u0082\u0000\u0000\u03a2\u03a1\u0001\u0000\u0000\u0000\u03a2\u03a3\u0001" +
                    "\u0000\u0000\u0000\u03a3\u03a4\u0001\u0000\u0000\u0000\u03a4\u03a5\u0005" +
                    "M\u0000\u0000\u03a5\u03a6\u0005\u0088\u0000\u0000\u03a6\u03a7\u0005\u0088" +
                    "\u0000\u0000\u03a7\u0093\u0001\u0000\u0000\u0000\u03a8\u03aa\u0005\u0082" +
                    "\u0000\u0000\u03a9\u03a8\u0001\u0000\u0000\u0000\u03a9\u03aa\u0001\u0000" +
                    "\u0000\u0000\u03aa\u03ab\u0001\u0000\u0000\u0000\u03ab\u03ac\u0005N\u0000" +
                    "\u0000\u03ac\u03ad\u0005\u0088\u0000\u0000\u03ad\u0095\u0001\u0000\u0000" +
                    "\u0000\u03ae\u03b0\u0005\u0082\u0000\u0000\u03af\u03ae\u0001\u0000\u0000" +
                    "\u0000\u03af\u03b0\u0001\u0000\u0000\u0000\u03b0\u03b1\u0001\u0000\u0000" +
                    "\u0000\u03b1\u03b2\u0005O\u0000\u0000\u03b2\u03b3\u0003\u009eO\u0000\u03b3" +
                    "\u0097\u0001\u0000\u0000\u0000\u03b4\u03b6\u0005\u0082\u0000\u0000\u03b5" +
                    "\u03b4\u0001\u0000\u0000\u0000\u03b5\u03b6\u0001\u0000\u0000\u0000\u03b6" +
                    "\u03b7\u0001\u0000\u0000\u0000\u03b7\u03b8\u0005P\u0000\u0000\u03b8\u03b9" +
                    "\u0003\u009eO\u0000\u03b9\u0099\u0001\u0000\u0000\u0000\u03ba\u03bc\u0005" +
                    "\u0082\u0000\u0000\u03bb\u03ba\u0001\u0000\u0000\u0000\u03bb\u03bc\u0001" +
                    "\u0000\u0000\u0000\u03bc\u03bd\u0001\u0000\u0000\u0000\u03bd\u03be\u0005" +
                    "Q\u0000\u0000\u03be\u03bf\u0005\u0088\u0000\u0000\u03bf\u03c0\u0005\u0088" +
                    "\u0000\u0000\u03c0\u009b\u0001\u0000\u0000\u0000\u03c1\u03c3\u0005\u0082" +
                    "\u0000\u0000\u03c2\u03c1\u0001\u0000\u0000\u0000\u03c2\u03c3\u0001\u0000" +
                    "\u0000\u0000\u03c3\u03c4\u0001\u0000\u0000\u0000\u03c4\u03c5\u0005R\u0000" +
                    "\u0000\u03c5\u03c6\u0005\u0088\u0000\u0000\u03c6\u03c7\u0005\u0088\u0000" +
                    "\u0000\u03c7\u03c8\u0005\u0088\u0000\u0000\u03c8\u03c9\u0005\u0088\u0000" +
                    "\u0000\u03c9\u009d\u0001\u0000\u0000\u0000\u03ca\u03cc\u0005\u0082\u0000" +
                    "\u0000\u03cb\u03ca\u0001\u0000\u0000\u0000\u03cb\u03cc\u0001\u0000\u0000" +
                    "\u0000\u03cc\u03d1\u0001\u0000\u0000\u0000\u03cd\u03ce\u0005S\u0000\u0000" +
                    "\u03ce\u03d0\u0005\u0085\u0000\u0000\u03cf\u03cd\u0001\u0000\u0000\u0000" +
                    "\u03d0\u03d3\u0001\u0000\u0000\u0000\u03d1\u03cf\u0001\u0000\u0000\u0000" +
                    "\u03d1\u03d2\u0001\u0000\u0000\u0000\u03d2\u009f\u0001\u0000\u0000\u0000" +
                    "\u03d3\u03d1\u0001\u0000\u0000\u0000\u03d4\u03d6\u0005\u0082\u0000\u0000" +
                    "\u03d5\u03d4\u0001\u0000\u0000\u0000\u03d5\u03d6\u0001\u0000\u0000\u0000" +
                    "\u03d6\u03d7\u0001\u0000\u0000\u0000\u03d7\u03d8\u0005T\u0000\u0000\u03d8" +
                    "\u03d9\u0005\u0087\u0000\u0000\u03d9\u00a1\u0001\u0000\u0000\u0000\u03da" +
                    "\u03dc\u0005\u0082\u0000\u0000\u03db\u03da\u0001\u0000\u0000\u0000\u03db" +
                    "\u03dc\u0001\u0000\u0000\u0000\u03dc\u03dd\u0001\u0000\u0000\u0000\u03dd" +
                    "\u03de\u0005U\u0000\u0000\u03de\u03df\u0005\u0087\u0000\u0000\u03df\u00a3" +
                    "\u0001\u0000\u0000\u0000\u03e0\u03e2\u0005\u0082\u0000\u0000\u03e1\u03e0" +
                    "\u0001\u0000\u0000\u0000\u03e1\u03e2\u0001\u0000\u0000\u0000\u03e2\u03e3" +
                    "\u0001\u0000\u0000\u0000\u03e3\u03e4\u0005V\u0000\u0000\u03e4\u03e5\u0005" +
                    "\u0087\u0000\u0000\u03e5\u00a5\u0001\u0000\u0000\u0000\u03e6\u03e8\u0005" +
                    "\u0082\u0000\u0000\u03e7\u03e6\u0001\u0000\u0000\u0000\u03e7\u03e8\u0001" +
                    "\u0000\u0000\u0000\u03e8\u03e9\u0001\u0000\u0000\u0000\u03e9\u03eb\u0005" +
                    "W\u0000\u0000\u03ea\u03ec\u0005X\u0000\u0000\u03eb\u03ea\u0001\u0000\u0000" +
                    "\u0000\u03eb\u03ec\u0001\u0000\u0000\u0000\u03ec\u03ed\u0001\u0000\u0000" +
                    "\u0000\u03ed\u03ee\u0005\u0087\u0000\u0000\u03ee\u00a7\u0001\u0000\u0000" +
                    "\u0000\u03ef\u03f1\u0005\u0082\u0000\u0000\u03f0\u03ef\u0001\u0000\u0000" +
                    "\u0000\u03f0\u03f1\u0001\u0000\u0000\u0000\u03f1\u03f2\u0001\u0000\u0000" +
                    "\u0000\u03f2\u03f3\u0005Y\u0000\u0000\u03f3\u03f4\u0005\u0087\u0000\u0000" +
                    "\u03f4\u00a9\u0001\u0000\u0000\u0000\u03f5\u03f7\u0005\u0082\u0000\u0000" +
                    "\u03f6\u03f5\u0001\u0000\u0000\u0000\u03f6\u03f7\u0001\u0000\u0000\u0000" +
                    "\u03f7\u03f8\u0001\u0000\u0000\u0000\u03f8\u03f9\u0005Z\u0000\u0000\u03f9" +
                    "\u00ab\u0001\u0000\u0000\u0000\u03fa\u03fc\u0005\u0082\u0000\u0000\u03fb" +
                    "\u03fa\u0001\u0000\u0000\u0000\u03fb\u03fc\u0001\u0000\u0000\u0000\u03fc" +
                    "\u03fd\u0001\u0000\u0000\u0000\u03fd\u03fe\u0005[\u0000\u0000\u03fe\u00ad" +
                    "\u0001\u0000\u0000\u0000\u03ff\u0401\u0005\u0082\u0000\u0000\u0400\u03ff" +
                    "\u0001\u0000\u0000\u0000\u0400\u0401\u0001\u0000\u0000\u0000\u0401\u0402" +
                    "\u0001\u0000\u0000\u0000\u0402\u0403\u0005\\\u0000\u0000\u0403\u0404\u0005" +
                    "\u0088\u0000\u0000\u0404\u0405\u0005\u0088\u0000\u0000\u0405\u00af\u0001" +
                    "\u0000\u0000\u0000\u0406\u0408\u0005\u0082\u0000\u0000\u0407\u0406\u0001" +
                    "\u0000\u0000\u0000\u0407\u0408\u0001\u0000\u0000\u0000\u0408\u0409\u0001" +
                    "\u0000\u0000\u0000\u0409\u040a\u0005]\u0000\u0000\u040a\u040b\u0005\u0086" +
                    "\u0000\u0000\u040b\u00b1\u0001\u0000\u0000\u0000\u040c\u040e\u0005\u0082" +
                    "\u0000\u0000\u040d\u040c\u0001\u0000\u0000\u0000\u040d\u040e\u0001\u0000" +
                    "\u0000\u0000\u040e\u040f\u0001\u0000\u0000\u0000\u040f\u0410\u0005^\u0000" +
                    "\u0000\u0410\u0411\u0005\u0081\u0000\u0000\u0411\u0412\u0005\u008a\u0000" +
                    "\u0000\u0412\u00b3\u0001\u0000\u0000\u0000\u0413\u0415\u0005\u0082\u0000" +
                    "\u0000\u0414\u0413\u0001\u0000\u0000\u0000\u0414\u0415\u0001\u0000\u0000" +
                    "\u0000\u0415\u0416\u0001\u0000\u0000\u0000\u0416\u0417\u0005_\u0000\u0000" +
                    "\u0417\u0418\u0005\u0081\u0000\u0000\u0418\u00b5\u0001\u0000\u0000\u0000" +
                    "\u0419\u041b\u0005\u0082\u0000\u0000\u041a\u0419\u0001\u0000\u0000\u0000" +
                    "\u041a\u041b\u0001\u0000\u0000\u0000\u041b\u041c\u0001\u0000\u0000\u0000" +
                    "\u041c\u041d\u0005`\u0000\u0000\u041d\u00b7\u0001\u0000\u0000\u0000\u041e" +
                    "\u0420\u0005\u0082\u0000\u0000\u041f\u041e\u0001\u0000\u0000\u0000\u041f" +
                    "\u0420\u0001\u0000\u0000\u0000\u0420\u0421\u0001\u0000\u0000\u0000\u0421" +
                    "\u0422\u0005a\u0000\u0000\u0422\u00b9\u0001\u0000\u0000\u0000\u0423\u0425" +
                    "\u0005\u0082\u0000\u0000\u0424\u0423\u0001\u0000\u0000\u0000\u0424\u0425" +
                    "\u0001\u0000\u0000\u0000\u0425\u0426\u0001\u0000\u0000\u0000\u0426\u0427" +
                    "\u0005b\u0000\u0000\u0427\u0428\u0007\u0003\u0000\u0000\u0428\u00bb\u0001" +
                    "\u0000\u0000\u0000\u0429\u042b\u0005\u0082\u0000\u0000\u042a\u0429\u0001" +
                    "\u0000\u0000\u0000\u042a\u042b\u0001\u0000\u0000\u0000\u042b\u042c\u0001" +
                    "\u0000\u0000\u0000\u042c\u042d\u0005c\u0000\u0000\u042d\u00bd\u0001\u0000" +
                    "\u0000\u0000\u042e\u0430\u0005\u0082\u0000\u0000\u042f\u042e\u0001\u0000" +
                    "\u0000\u0000\u042f\u0430\u0001\u0000\u0000\u0000\u0430\u0431\u0001\u0000" +
                    "\u0000\u0000\u0431\u0432\u0005c\u0000\u0000\u0432\u0433\u0005\u0088\u0000" +
                    "\u0000\u0433\u00bf\u0001\u0000\u0000\u0000\u0434\u0436\u0005\u0082\u0000" +
                    "\u0000\u0435\u0434\u0001\u0000\u0000\u0000\u0435\u0436\u0001\u0000\u0000" +
                    "\u0000\u0436\u0437\u0001\u0000\u0000\u0000\u0437\u0438\u0005d\u0000\u0000" +
                    "\u0438\u00c1\u0001\u0000\u0000\u0000\u0439\u043b\u0005\u0082\u0000\u0000" +
                    "\u043a\u0439\u0001\u0000\u0000\u0000\u043a\u043b\u0001\u0000\u0000\u0000" +
                    "\u043b\u043c\u0001\u0000\u0000\u0000\u043c\u043d\u0005e\u0000\u0000\u043d" +
                    "\u00c3\u0001\u0000\u0000\u0000\u043e\u0440\u0005\u0082\u0000\u0000\u043f" +
                    "\u043e\u0001\u0000\u0000\u0000\u043f\u0440\u0001\u0000\u0000\u0000\u0440" +
                    "\u0441\u0001\u0000\u0000\u0000\u0441\u0442\u0005f\u0000\u0000\u0442\u0443" +
                    "\u0005\u0088\u0000\u0000\u0443\u00c5\u0001\u0000\u0000\u0000\u0444\u0446" +
                    "\u0005\u0082\u0000\u0000\u0445\u0444\u0001\u0000\u0000\u0000\u0445\u0446" +
                    "\u0001\u0000\u0000\u0000\u0446\u0447\u0001\u0000\u0000\u0000\u0447\u044e" +
                    "\u0005g\u0000\u0000\u0448\u0449\u0005\u0088\u0000\u0000\u0449\u044a\u0005" +
                    "\u0088\u0000\u0000\u044a\u044c\u0005\u0088\u0000\u0000\u044b\u044d\u0005" +
                    "h\u0000\u0000\u044c\u044b\u0001\u0000\u0000\u0000\u044c\u044d\u0001\u0000" +
                    "\u0000\u0000\u044d\u044f\u0001\u0000\u0000\u0000\u044e\u0448\u0001\u0000" +
                    "\u0000\u0000\u044e\u044f\u0001\u0000\u0000\u0000\u044f\u00c7\u0001\u0000" +
                    "\u0000\u0000\u0450\u0452\u0005\u0082\u0000\u0000\u0451\u0450\u0001\u0000" +
                    "\u0000\u0000\u0451\u0452\u0001\u0000\u0000\u0000\u0452\u0453\u0001\u0000" +
                    "\u0000\u0000\u0453\u0454\u0005i\u0000\u0000\u0454\u0455\u0005\u0088\u0000" +
                    "\u0000\u0455\u00c9\u0001\u0000\u0000\u0000\u0456\u0458\u0005\u0082\u0000" +
                    "\u0000\u0457\u0456\u0001\u0000\u0000\u0000\u0457\u0458\u0001\u0000\u0000" +
                    "\u0000\u0458\u0459\u0001\u0000\u0000\u0000\u0459\u045a\u0005j\u0000\u0000" +
                    "\u045a\u00cb\u0001\u0000\u0000\u0000\u045b\u045d\u0005\u0082\u0000\u0000" +
                    "\u045c\u045b\u0001\u0000\u0000\u0000\u045c\u045d\u0001\u0000\u0000\u0000" +
                    "\u045d\u045e\u0001\u0000\u0000\u0000\u045e\u045f\u0005k\u0000\u0000\u045f" +
                    "\u0460\u0005\u0088\u0000\u0000\u0460\u0461\u0005\u0088\u0000\u0000\u0461" +
                    "\u0462\u0005\u0088\u0000\u0000\u0462\u00cd\u0001\u0000\u0000\u0000\u0463" +
                    "\u0465\u0005\u0082\u0000\u0000\u0464\u0463\u0001\u0000\u0000\u0000\u0464" +
                    "\u0465\u0001\u0000\u0000\u0000\u0465\u0466\u0001\u0000\u0000\u0000\u0466" +
                    "\u0467\u0005l\u0000\u0000\u0467\u0468\u0007\u0004\u0000\u0000\u0468\u00cf" +
                    "\u0001\u0000\u0000\u0000\u0469\u046b\u0005\u0082\u0000\u0000\u046a\u0469" +
                    "\u0001\u0000\u0000\u0000\u046a\u046b\u0001\u0000\u0000\u0000\u046b\u046c" +
                    "\u0001\u0000\u0000\u0000\u046c\u046d\u0005p\u0000\u0000\u046d\u046e\u0005" +
                    "\u0087\u0000\u0000\u046e\u00d1\u0001\u0000\u0000\u0000\u046f\u0471\u0005" +
                    "\u0082\u0000\u0000\u0470\u046f\u0001\u0000\u0000\u0000\u0470\u0471\u0001" +
                    "\u0000\u0000\u0000\u0471\u0472\u0001\u0000\u0000\u0000\u0472\u0473\u0005" +
                    "q\u0000\u0000\u0473\u0474\u0005\u0088\u0000\u0000\u0474\u00d3\u0001\u0000" +
                    "\u0000\u0000\u0475\u0477\u0005\u0082\u0000\u0000\u0476\u0475\u0001\u0000" +
                    "\u0000\u0000\u0476\u0477\u0001\u0000\u0000\u0000\u0477\u0478\u0001\u0000" +
                    "\u0000\u0000\u0478\u0479\u0005r\u0000\u0000\u0479\u047a\u0007\u0005\u0000" +
                    "\u0000\u047a\u00d5\u0001\u0000\u0000\u0000\u047b\u047d\u0005\u0082\u0000" +
                    "\u0000\u047c\u047b\u0001\u0000\u0000\u0000\u047c\u047d\u0001\u0000\u0000" +
                    "\u0000\u047d\u047e\u0001\u0000\u0000\u0000\u047e\u047f\u0005v\u0000\u0000" +
                    "\u047f\u0480\u0005\u0088\u0000\u0000\u0480\u0481\u0005\u0088\u0000\u0000" +
                    "\u0481\u00d7\u0001\u0000\u0000\u0000\u0482\u0484\u0005\u0082\u0000\u0000" +
                    "\u0483\u0482\u0001\u0000\u0000\u0000\u0483\u0484\u0001\u0000\u0000\u0000" +
                    "\u0484\u0485\u0001\u0000\u0000\u0000\u0485\u0486\u0005w\u0000\u0000\u0486" +
                    "\u0487\u0005\u0087\u0000\u0000\u0487\u00d9\u0001\u0000\u0000\u0000\u0488" +
                    "\u048a\u0005\u0082\u0000\u0000\u0489\u0488\u0001\u0000\u0000\u0000\u0489" +
                    "\u048a\u0001\u0000\u0000\u0000\u048a\u048b\u0001\u0000\u0000\u0000\u048b" +
                    "\u048c\u0005x\u0000\u0000\u048c\u048d\u0005\u0088\u0000\u0000\u048d\u00db" +
                    "\u0001\u0000\u0000\u0000\u048e\u0490\u0005\u0082\u0000\u0000\u048f\u048e" +
                    "\u0001\u0000\u0000\u0000\u048f\u0490\u0001\u0000\u0000\u0000\u0490\u0491" +
                    "\u0001\u0000\u0000\u0000\u0491\u0492\u0005y\u0000\u0000\u0492\u0493\u0005" +
                    "\u0088\u0000\u0000\u0493\u00dd\u0001\u0000\u0000\u0000\u0494\u0496\u0005" +
                    "\u0082\u0000\u0000\u0495\u0494\u0001\u0000\u0000\u0000\u0495\u0496\u0001" +
                    "\u0000\u0000\u0000\u0496\u0497\u0001\u0000\u0000\u0000\u0497\u0498\u0005" +
                    "z\u0000\u0000\u0498\u00df\u0001\u0000\u0000\u0000\u0499\u049b\u0005\u0082" +
                    "\u0000\u0000\u049a\u0499\u0001\u0000\u0000\u0000\u049a\u049b\u0001\u0000" +
                    "\u0000\u0000\u049b\u049c\u0001\u0000\u0000\u0000\u049c\u049d\u0005{\u0000" +
                    "\u0000\u049d\u049e\u0005\u0088\u0000\u0000\u049e\u00e1\u0001\u0000\u0000" +
                    "\u0000\u049f\u04a1\u0005\u0082\u0000\u0000\u04a0\u049f\u0001\u0000\u0000" +
                    "\u0000\u04a0\u04a1\u0001\u0000\u0000\u0000\u04a1\u04a2\u0001\u0000\u0000" +
                    "\u0000\u04a2\u04a3\u0005|\u0000\u0000\u04a3\u04a4\u0007\u0006\u0000\u0000" +
                    "\u04a4\u00e3\u0001\u0000\u0000\u0000\u04a5\u04a7\u0005\u0082\u0000\u0000" +
                    "\u04a6\u04a5\u0001\u0000\u0000\u0000\u04a6\u04a7\u0001\u0000\u0000\u0000" +
                    "\u04a7\u04a8\u0001\u0000\u0000\u0000\u04a8\u04a9\u0005~\u0000\u0000\u04a9" +
                    "\u04aa\u0005\u0088\u0000\u0000\u04aa\u00e5\u0001\u0000\u0000\u0000\u04ab" +
                    "\u04ad\u0005\u0082\u0000\u0000\u04ac\u04ab\u0001\u0000\u0000\u0000\u04ac" +
                    "\u04ad\u0001\u0000\u0000\u0000\u04ad\u04ae\u0001\u0000\u0000\u0000\u04ae" +
                    "\u04af\u0005\u007f\u0000\u0000\u04af\u00e7\u0001\u0000\u0000\u0000\u04b0" +
                    "\u04b1\u0005\u0080\u0000\u0000\u04b1\u04b2\u0005\u0086\u0000\u0000\u04b2" +
                    "\u04b3\u0005\u0085\u0000\u0000\u04b3\u00e9\u0001\u0000\u0000\u0000\u008d" +
                    "\u00ee\u0162\u0168\u016d\u0172\u0179\u017d\u0184\u018a\u0191\u0198\u019e" +
                    "\u01a5\u01ac\u01b2\u01b9\u01bf\u01c5\u01cb\u01d2\u01d8\u01df\u01e6\u01ec" +
                    "\u01f3\u01f9\u01ff\u0205\u020c\u0212\u0219\u0220\u0226\u022d\u0234\u023a" +
                    "\u0241\u0249\u024f\u0257\u0261\u0267\u0271\u0278\u027e\u0285\u028c\u0292" +
                    "\u0299\u02a0\u02a6\u02ad\u02b3\u02b9\u02bf\u02c6\u02cc\u02d3\u02d9\u02df" +
                    "\u02e9\u02ef\u02f6\u02fb\u02fe\u0304\u0309\u030f\u0314\u031a\u0320\u0326" +
                    "\u032b\u0330\u0335\u033b\u0340\u0345\u034a\u034d\u0354\u035b\u035f\u0362" +
                    "\u0369\u036c\u0371\u0377\u037d\u0383\u0388\u038f\u0395\u039c\u03a2\u03a9" +
                    "\u03af\u03b5\u03bb\u03c2\u03cb\u03d1\u03d5\u03db\u03e1\u03e7\u03eb\u03f0" +
                    "\u03f6\u03fb\u0400\u0407\u040d\u0414\u041a\u041f\u0424\u042a\u042f\u0435" +
                    "\u043a\u043f\u0445\u044c\u044e\u0451\u0457\u045c\u0464\u046a\u0470\u0476" +
                    "\u047c\u0483\u0489\u048f\u0495\u049a\u04a0\u04a6\u04ac";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}