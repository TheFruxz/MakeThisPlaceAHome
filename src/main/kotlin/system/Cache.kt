package system

import de.moltenKt.paper.structure.app.AppCache
import de.moltenKt.paper.structure.app.cache.CacheDepthLevel
import de.moltenKt.paper.structure.app.cache.CacheDepthLevel.CLEAR
import de.moltenKt.paper.structure.app.cache.CacheDepthLevel.DUMP
import org.bukkit.Location
import java.util.*

object Cache : AppCache {

	var homeCache = mapOf<UUID, Location>()

	override fun dropEntityData(entityIdentity: UUID, dropDepth: CacheDepthLevel) {
		when {
			dropDepth.isDeeperThanOrEquals(DUMP) -> {
				homeCache -= entityIdentity
			}
		}
	}

	override fun dropEverything(dropDepth: CacheDepthLevel) {
		when {
			dropDepth.isDeeperThanOrEquals(CLEAR) -> {
				homeCache = emptyMap()
			}
		}
	}

}