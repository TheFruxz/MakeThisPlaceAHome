package home

import MTPAH
import de.moltenKt.core.extension.classType.UUID
import de.moltenKt.core.extension.data.fromJson
import de.moltenKt.core.extension.data.toJson
import de.moltenKt.core.extension.div
import de.moltenKt.core.extension.tryOrNull
import de.moltenKt.paper.tool.data.file.MoltenFileSystem
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import system.Cache.homeCache
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText

object HomeManager {

	val homePath = MoltenFileSystem.appPath(MTPAH) / "homes.json"

	fun loadToCache(): Map<UUID, Location> {
		homeCache = tryOrNull { homePath.readText().fromJson() } ?: mapOf<UUID, Location>().also {
			homePath.parent.createDirectories()
			homePath.writeText(it.toJson())
		}
		return homeCache
	}

	fun playerHome(player: OfflinePlayer) = homeCache[player.uniqueId] ?: loadToCache()[player.uniqueId]

	fun playerHome(player: OfflinePlayer, home: Location?) {

		if (home != null) {
			homeCache += player.uniqueId to home
		} else
			homeCache -= player.uniqueId

		homePath.writeText(homeCache.toJson())

	}

}