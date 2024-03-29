package me.afoolslove.metalmaxre.editors.map.events;


import me.afoolslove.metalmaxre.editors.map.CameraMapPoint;
import me.afoolslove.metalmaxre.utils.Point2B;

/**
 * 罗克东部涨潮和退潮的4个4*4tile
 *
 * @author AFoolLove
 */
public class WorldMapInteractiveEvent extends CameraMapPoint {
    public byte event;
    public byte direction;
    public Point2B aPoint;
    public byte[] aTrue;
    public byte[] aFalse;

    public Point2B bPoint;
    public byte[] bTrue;
    public byte[] bFalse;

    public WorldMapInteractiveEvent() {
        super(0, 0, 0);
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    public byte getDirection() {
        return direction;
    }

    public void setEvent(byte event) {
        this.event = event;
    }

    public byte getEvent() {
        return event;
    }
}
