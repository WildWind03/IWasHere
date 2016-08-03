package com.noveogroup.teamzolotov.iwashere.util;

import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.model.Region;

import java.util.LinkedList;

public final class RegionUtils {

    private RegionUtils() {
        throw new UnsupportedOperationException("Trying to create instance of utility class");
    }
    public static LinkedList<Region> initRegions() {
        LinkedList<Region> regions = new LinkedList<>();
        regions.add(new Region(-140290));
        regions.add(new Region(-89331));
        regions.add(new Region(-72192));
        regions.add(new Region(-81993));
        regions.add(new Region(-72193));
        regions.add(new Region(-72223));
        regions.add(new Region(-190090));
        regions.add(new Region(-140337));
        regions.add(new Region(-109878));
        regions.add(new Region(-109876));
        regions.add(new Region(-108081));
        regions.add(new Region(-2095259));
        regions.add(new Region(-85617));
        regions.add(new Region(-115134));
        regions.add(new Region(-115114));
        regions.add(new Region(-140292));
        regions.add(new Region(-140291));
        regions.add(new Region(-144764));
        regions.add(new Region(-140295));
        regions.add(new Region(-140294));
        regions.add(new Region(-115135));
        regions.add(new Region(-77677));
        regions.add(new Region(-79379));
        regions.add(new Region(-77687));
        regions.add(new Region(-2099216));
        regions.add(new Region(-151234));
        regions.add(new Region(-151231));
        regions.add(new Region(-151228));
        regions.add(new Region(-191706));
        regions.add(new Region(-140296));
        regions.add(new Region(-393980));
        regions.add(new Region(-337422));
        regions.add(new Region(-274048));
        regions.add(new Region(-176095));
        regions.add(new Region(-115136));
        regions.add(new Region(-115106));
        regions.add(new Region(-72224));
        regions.add(new Region(-81996));
        regions.add(new Region(-72181));
        regions.add(new Region(-151223));
        regions.add(new Region(-253256));
        regions.add(new Region(-102269));
        regions.add(new Region(-85963));
        regions.add(new Region(-83184));
        regions.add(new Region(-81995));
        regions.add(new Region(-72197));
        regions.add(new Region(-72180));
        regions.add(new Region(-72169));
        regions.add(new Region(-71950));
        regions.add(new Region(-51490));
        regions.add(new Region(-151233));
        regions.add(new Region(-151225));
        regions.add(new Region(-147167));
        regions.add(new Region(-147166));
        regions.add(new Region(-72196));
        regions.add(new Region(-81997));
        regions.add(new Region(-155262));
        regions.add(new Region(-112819));
        regions.add(new Region(-394235));
        regions.add(new Region(-253252));
        regions.add(new Region(-110032));
        regions.add(new Region(-109879));
        regions.add(new Region(-109877));
        regions.add(new Region(-103906));
        regions.add(new Region(-108083));
        regions.add(new Region(-108082));
        regions.add(new Region(-85606));
        regions.add(new Region(-77665));
        regions.add(new Region(-115100));
        regions.add(new Region(-80513));
        regions.add(new Region(-79374));
        regions.add(new Region(-81994));
        regions.add(new Region(-77669));
        regions.add(new Region(-72194));
        regions.add(new Region(-72182));
        regions.add(new Region(-72195));
        regions.add(new Region(-144763));
        regions.add(new Region(-145729));
        regions.add(new Region(-145730));
        regions.add(new Region(-145454));
        regions.add(new Region(-145194));
        regions.add(new Region(-190911));
        regions.add(new Region(-145195));

        return regions;
    }

    public static int getRegionIconResource(int osmId) {
        switch (osmId) {
            case -140290:
                return R.drawable.kurgan;
            case -89331:
                return R.drawable.novgorod;
            case -72192:
                return R.drawable.ulyanovsk;
            case -81993:
                return R.drawable.tula;
            case -72193:
                return R.drawable.saratov;
            case -72223:
                return R.drawable.kursk;
            case -190090:
                return R.drawable.krasnoyarsk;
            case -140337:
                return R.drawable.arhangel;
            case -109878:
                return R.drawable.karachaevo_cherkessia;
            case -109876:
                return R.drawable.dagestan;
            case -108081:
                return R.drawable.stavropol;
            case -2095259:
                return R.drawable.tver;
            case -85617:
                return R.drawable.ivanovo;
            case -115134:
                return R.drawable.udmurtia;
            case -115114:
                return R.drawable.mariy_el;
            case -140292:
                return R.drawable.omsk;
            case -140291:
                return R.drawable.tumen;
            case -144764:
                return R.drawable.altai_krai;
            case -140295:
                return R.drawable.tomsk;
            case -140294:
                return R.drawable.novosibirsk;
            case -115135:
                return R.drawable.perm;
            case -77677:
                return R.drawable.baskortostan;
            case -79379:
                return R.drawable.sverdlovsk;
            case -77687:
                return R.drawable.chelyabinsk;
            case -2099216:
                return R.drawable.murmansk;
            case -151234:
                return R.drawable.saha;
            case -151231:
                return R.drawable.chukotka;
            case -151228:
                return R.drawable.magadan;
            case -191706:
                return R.drawable.yamalo_nenets;
            case -140296:
                return R.drawable.yugra;
            case -393980:
                return R.drawable.karelia;
            case -337422:
                return R.drawable.piter;
            case -274048:
                return R.drawable.nenec;
            case -176095:
                return R.drawable.leningrad;
            case -115136:
                return R.drawable.komi;
            case -115106:
                return R.drawable.vologda;
            case -72224:
                return R.drawable.orel;
            case -81996:
                return R.drawable.smolensk;
            case -72181:
                return R.drawable.voronezh;
            case -151223:
                return R.drawable.habarovsk;
            case -253256:
                return R.drawable.adygea;
            case -102269:
                return R.drawable.moscow_city;
            case -85963:
                return R.drawable.kostroma;
            case -83184:
                return R.drawable.belgorod;
            case -81995:
                return R.drawable.kaluga;
            case -72197:
                return R.drawable.vladimir;
            case -72180:
                return R.drawable.tambov;
            case -72169:
                return R.drawable.lipetsk;
            case -71950:
                return R.drawable.ryazan;
            case -51490:
                return R.drawable.moscow_obl;
            case -151233:
                return R.drawable.kamchatka;
            case -151225:
                return R.drawable.primorsky;
            case -147167:
                return R.drawable.yevrey;
            case -147166:
                return R.drawable.amur;
            case -72196:
                return R.drawable.mordovia;
            case -81997:
                return R.drawable.bryansk;
            case -155262:
                return R.drawable.pskov;
            case -112819:
                return R.drawable.astrahan;
            case -394235:
                return R.drawable.sahalin;
            case -253252:
                return R.drawable.ingushetia;
            case -110032:
                return R.drawable.sev_osetia;
            case -109879:
                return R.drawable.kabardino_balkaria;
            case -109877:
                return R.drawable.chechnya;
            case -103906:
                return R.drawable.kaliningrad;
            case -108083:
                return R.drawable.kalmykia;
            case -108082:
                return R.drawable.krasnodar;
            case -85606:
                return R.drawable.rostov;
            case -77665:
                return R.drawable.volgograd;
            case -115100:
                return R.drawable.kirov;
            case -80513:
                return R.drawable.chuvashia;
            case -79374:
                return R.drawable.tatarstan;
            case -81994:
                return R.drawable.yaroslavl;
            case -77669:
                return R.drawable.orenburg;
            case -72194:
                return R.drawable.samara;
            case -72182:
                return R.drawable.penza;
            case -72195:
                return R.drawable.nizhny;
            case -144763:
                return R.drawable.kemerovo;
            case -145729:
                return R.drawable.buryatiya;
            case -145730:
                return R.drawable.zabaikal;
            case -145454:
                return R.drawable.irkutsk;
            case -145194:
                return R.drawable.altai_rep;
            case -190911:
                return R.drawable.hakasia;
            case -145195:
                return R.drawable.tyva;


            default:
                return R.drawable.common_google_signin_btn_icon_dark_normal;
        }
    }

    public static int getRegionNameResource(int osmId) {
        switch (osmId) {

            case -140290:
                return R.string.kurgan;
            case -89331:
                return R.string.novgorod;
            case -72192:
                return R.string.ulyanovsk;
            case -81993:
                return R.string.tula;
            case -72193:
                return R.string.saratov;
            case -72223:
                return R.string.kursk;
            case -190090:
                return R.string.krasnoyarsk;
            case -140337:
                return R.string.arhangel;
            case -109878:
                return R.string.karachaevo_cherkessia;
            case -109876:
                return R.string.dagestan;
            case -108081:
                return R.string.stavropol;
            case -2095259:
                return R.string.tver;
            case -85617:
                return R.string.ivanovo;
            case -115134:
                return R.string.udmurtia;
            case -115114:
                return R.string.mariy_el;
            case -140292:
                return R.string.omsk;
            case -140291:
                return R.string.tumen;
            case -144764:
                return R.string.altai_krai;
            case -140295:
                return R.string.tomsk;
            case -140294:
                return R.string.novosibirsk;
            case -115135:
                return R.string.perm;
            case -77677:
                return R.string.bashkortostan;
            case -79379:
                return R.string.sverdlovsk;
            case -77687:
                return R.string.chelyabinsk;
            case -2099216:
                return R.string.murmansk;
            case -151234:
                return R.string.saha;
            case -151231:
                return R.string.chukotka;
            case -151228:
                return R.string.magadan;
            case -191706:
                return R.string.yamalo_nenets;
            case -140296:
                return R.string.yugra;
            case -393980:
                return R.string.karelia;
            case -337422:
                return R.string.piter;
            case -274048:
                return R.string.nenec;
            case -176095:
                return R.string.leningrad;
            case -115136:
                return R.string.komi;
            case -115106:
                return R.string.vologda;
            case -72224:
                return R.string.orel;
            case -81996:
                return R.string.smolensk;
            case -72181:
                return R.string.voronezh;
            case -151223:
                return R.string.habarovsk;
            case -253256:
                return R.string.adygea;
            case -102269:
                return R.string.moscow_city;
            case -85963:
                return R.string.kostroma;
            case -83184:
                return R.string.belgorod;
            case -81995:
                return R.string.kaluga;
            case -72197:
                return R.string.vladimir;
            case -72180:
                return R.string.tambov;
            case -72169:
                return R.string.lipetsk;
            case -71950:
                return R.string.ryazan;
            case -51490:
                return R.string.moscow_obl;
            case -151233:
                return R.string.kamchatka;
            case -151225:
                return R.string.primorsky;
            case -147167:
                return R.string.yevrey;
            case -147166:
                return R.string.amur;
            case -72196:
                return R.string.mordovia;
            case -81997:
                return R.string.bryansk;
            case -155262:
                return R.string.pskov;
            case -112819:
                return R.string.astrahan;
            case -394235:
                return R.string.sahalin;
            case -253252:
                return R.string.ingushetia;
            case -110032:
                return R.string.sev_osetia;
            case -109879:
                return R.string.kabardino_balkaria;
            case -109877:
                return R.string.chechnya;
            case -103906:
                return R.string.kaliningrad;
            case -108083:
                return R.string.kalmykia;
            case -108082:
                return R.string.krasnodar;
            case -85606:
                return R.string.rostov;
            case -77665:
                return R.string.volgograd;
            case -115100:
                return R.string.kirov;
            case -80513:
                return R.string.chuvashia;
            case -79374:
                return R.string.tatarstan;
            case -81994:
                return R.string.yaroslavl;
            case -77669:
                return R.string.orenburg;
            case -72194:
                return R.string.samara;
            case -72182:
                return R.string.penza;
            case -72195:
                return R.string.nizhny;
            case -144763:
                return R.string.kemerovo;
            case -145729:
                return R.string.buryatiya;
            case -145730:
                return R.string.zabaikal;
            case -145454:
                return R.string.irkutsk;
            case -145194:
                return R.string.altai_rep;
            case -190911:
                return R.string.hakasia;
            case -145195:
                return R.string.tyva;

            default:
                return R.string.app_name;
        }
    }

    public static int getRegionOsm(String okatoName) {
        switch (okatoName) {
            case "Курганская область" : return -140290;
            case "Новгородская область" : return -89331;
            case "Ульяновская область" : return -72192;
            case "Тульская область" : return -81993;
            case "Саратовская область" : return -72193;
            case "Курская область" : return -72223;
            case "Красноярский край" : return -190090;
            case "Архангельская область" : return -140337;
            case "Карачаево-Черкесия" : return -109878;
            case "Дагестан" : return -109876;
            case "Ставрополье" : return -108081;
            case "Тверская область" : return -2095259;
            case "Ивановская область" : return -85617;
            case "Удмуртия" : return -115134;
            case "Марий Эл" : return -115114;
            case "Омская область" : return -140292;
            case "Тюменская область" : return -140291;
            case "Алтайский край" : return -144764;
            case "Томская область" : return -140295;
            case "Новосибирская область" : return -140294;
            case "Пермский край" : return -115135;
            case "Башкортостан" : return -77677;
            case "Свердловская область" : return -79379;
            case "Челябинская область" : return -77687;
            case "Мурманская область" : return -2099216;
            case "Республика Саха (Якутия)" : return -151234;
            case "Чукотка" : return -151231;
            case "Магаданская область" : return -151228;
            case "Ямало-Ненецкий автономный округ" : return -191706;
            case "Ханты-Мансийский автономный округ - Югра" : return -140296;
            case "Республика Карелия" : return -393980;
            case "Санкт-Петербург" : return -337422;
            case "Ненецкий автономный округ" : return -274048;
            case "Ленинградская область" : return -176095;
            case "Республика Коми" : return -115136;
            case "Вологодская область" : return -115106;
            case "Орловская область" : return -72224;
            case "Смоленская область" : return -81996;
            case "Воронежская область" : return -72181;
            case "Хабаровский край" : return -151223;
            case "Адыгея" : return -253256;
            case "Москва" : return -102269;
            case "Костромская область" : return -85963;
            case "Белгородская область" : return -83184;
            case "Калужская область" : return -81995;
            case "Владимирская область" : return -72197;
            case "Тамбовская область" : return -72180;
            case "Липецкая область" : return -72169;
            case "Рязанская область" : return -71950;
            case "Московская область" : return -51490;
            case "Камчатский край" : return -151233;
            case "Приморский край" : return -151225;
            case "Еврейская автономная область" : return -147167;
            case "Амурская область" : return -147166;
            case "Мордовия" : return -72196;
            case "Брянская область" : return -81997;
            case "Псковская область" : return -155262;
            case "Астраханская область" : return -112819;
            case "Сахалинская область" : return -394235;
            case "Ингушетия" : return -253252;
            case "Северная Осетия - Алания" : return -110032;
            case "Кабардино-Балкария" : return -109879;
            case "Чечня" : return -109877;
            case "Калининградская область" : return -103906;
            case "Калмыкия" : return -108083;
            case "ЮФО" :
            case "Краснодарский край" : return -108082;
            case "Ростовская область" : return -85606;
            case "Волгоградская область" : return -77665;
            case "Кировская область" : return -115100;
            case "Чувашия" : return -80513;
            case "Татарстан" : return -79374;
            case "Ярославская область" : return -81994;
            case "Оренбургская область" : return -77669;
            case "Самарская область" : return -72194;
            case "Пензенская область" : return -72182;
            case "Нижегородская область" : return -72195;
            case "Кемеровская область" : return -144763;
            case "Бурятия" : return -145729;
            case "Забайкальский край" : return -145730;
            case "Иркутская область" : return -145454;
            case "Республика Алтай" : return -145194;
            case "Республика Хакасия" : return -190911;
            case "Тыва" : return -145195;
            default: return -1;
        }
    }
}
