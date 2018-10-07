package cc.yelinvan.photographhome.rycusboss.ptp.commands.nikon;

import cc.yelinvan.photographhome.rycusboss.ptp.NikonCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.Command;

public abstract class NikonCommand extends Command {
    protected NikonCamera camera;

    public NikonCommand(NikonCamera camera) {
        super(camera);
        this.camera = camera;
    }
}
