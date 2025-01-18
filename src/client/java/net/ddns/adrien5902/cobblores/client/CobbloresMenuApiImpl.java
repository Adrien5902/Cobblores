package net.ddns.adrien5902.cobblores.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class CobbloresMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<ConfigScreen> getModConfigScreenFactory() {
        return new ConfigScreen();
    }
}
