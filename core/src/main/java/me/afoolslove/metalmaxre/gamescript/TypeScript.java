package me.afoolslove.metalmaxre.gamescript;

public abstract class TypeScript extends BaseSpriteScript {
    public static final TypeScript STATIC = new Static();
    public static final TypeScript DYNAMIC = new Dynamic();

    private TypeScript() {
        super("");
    }

    public boolean isStatic() {
        return this == STATIC;
    }

    public boolean isDynamic() {
        return this == DYNAMIC;
    }


    private static class Static extends TypeScript {
    }

    private static class Dynamic extends TypeScript {
    }
}
