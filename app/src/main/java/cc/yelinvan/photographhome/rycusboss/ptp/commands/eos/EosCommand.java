package cc.yelinvan.photographhome.rycusboss.ptp.commands.eos;

import cc.yelinvan.photographhome.rycusboss.ptp.EosCamera;
import cc.yelinvan.photographhome.rycusboss.ptp.commands.Command;

public abstract class EosCommand extends Command {
    protected EosCamera camera;

    public EosCommand(EosCamera camera) {
        super(camera);
        this.camera = camera;
    }
}
