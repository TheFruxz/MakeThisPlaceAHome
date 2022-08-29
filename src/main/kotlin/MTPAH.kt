import de.moltenKt.paper.structure.app.App
import de.moltenKt.paper.structure.app.AppCompanion
import home.HomeManager
import interchange.HomeInterchange
import system.Cache

class MTPAH : App() {

	override val appIdentity = "mtpah"
	override val label = "MakeThisPlaceAHome"
	override val appCache = Cache
	override val companion = Companion

	override suspend fun hello() {
		HomeManager.loadToCache()

		add(HomeInterchange())

	}

	companion object : AppCompanion<MTPAH>() {
		override val predictedIdentity = "mtpah"
	}

}