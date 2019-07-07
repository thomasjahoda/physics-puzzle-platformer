<tileset version="1.2" tiledversion="1.2.3" name="atlas-tileset.tsx" tilewidth="32" tileheight="32" tilecount="48" columns="12">
	<image source="atlas-tileset.png" width="384" height="128"/>
	<terraintypes>
		<terrain name="platform" tile="0"/>
		<terrain name="platform-thin-border" tile="1"/>
		<terrain name="ugly-ball" tile="2"/>
		<terrain name="whatever" tile="3"/>
	</terraintypes>
	<tile id="0" type="platform-topleft" terrain=",,,0"/>
	<tile id="1" type="platform-top" terrain=",,0,0"/>
	<tile id="2" type="platform-topright" terrain=",,0,"/>
	<tile id="12" type="platform-left" terrain=",0,,0"/>
	<tile id="13" type="platform-middle" terrain="0,0,0,0"/>
	<tile id="14" type="platform-right" terrain="0,,0,"/>
	<tile id="24" type="platform-bottomleft" terrain=",0,,"/>
	<tile id="25" type="platform-bottom" terrain="0,0,,"/>
	<tile id="26" type="platform-bottomright" terrain="0,,,"/>
	<tile id="36" type="platform"/>
	<tile id="3" type="platform-thin-border-topleft" terrain=",,,1"/>
	<tile id="4" type="platform-thin-border-top" terrain=",,1,1"/>
	<tile id="5" type="platform-thin-border-topright" terrain=",,1,"/>
	<tile id="15" type="platform-thin-border-left" terrain=",1,,1"/>
	<tile id="16" type="platform-thin-border-middle" terrain="1,1,1,1"/>
	<tile id="17" type="platform-thin-border-right" terrain="1,,1,"/>
	<tile id="27" type="platform-thin-border-bottomleft" terrain=",1,,"/>
	<tile id="28" type="platform-thin-border-bottom" terrain="1,1,,"/>
	<tile id="29" type="platform-thin-border-bottomright" terrain="1,,,"/>
	<tile id="39" type="platform-thin-border"/>
	<tile id="6" type="ugly-ball-topleft" terrain=",,,2"/>
	<tile id="7" type="ugly-ball-top" terrain=",,2,2"/>
	<tile id="8" type="ugly-ball-topright" terrain=",,2,"/>
	<tile id="18" type="ugly-ball-left" terrain=",2,,2"/>
	<tile id="19" type="ugly-ball-middle" terrain="2,2,2,2"/>
	<tile id="20" type="ugly-ball-right" terrain="2,,2,"/>
	<tile id="30" type="ugly-ball-bottomleft" terrain=",2,,"/>
	<tile id="31" type="ugly-ball-bottom" terrain="2,2,,"/>
	<tile id="32" type="ugly-ball-bottomright" terrain="2,,,"/>
	<tile id="42" type="ugly-ball"/>
	<tile id="9" type="whatever-topleft" terrain=",,,3"/>
	<tile id="10" type="whatever-top" terrain=",,3,3"/>
	<tile id="11" type="whatever-topright" terrain=",,3,"/>
	<tile id="21" type="whatever-left" terrain=",3,,3"/>
	<tile id="22" type="whatever-middle" terrain="3,3,3,3"/>
	<tile id="23" type="whatever-right" terrain="3,,3,"/>
	<tile id="33" type="whatever-bottomleft" terrain=",3,,"/>
	<tile id="34" type="whatever-bottom" terrain="3,3,,"/>
	<tile id="35" type="whatever-bottomright" terrain="3,,,"/>
	<tile id="45" type="whatever"/>
</tileset>