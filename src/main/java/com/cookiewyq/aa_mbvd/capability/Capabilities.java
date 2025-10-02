package com.cookiewyq.aa_mbvd.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
    @CapabilityInject(IShowingEvidenceData.class)
    public static Capability<IShowingEvidenceData> SHOWING_EVIDENCE_DATA_CAPABILITY;
    public static Capability<IGetPlayerCourtRecordsTileEntityData> GET_PLAYER_COURT_RECORDS_TILE_ENTITY_CAPABILITY;
}
