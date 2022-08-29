package interchange

import de.moltenKt.paper.extension.display.notification
import de.moltenKt.paper.extension.effect.offset
import de.moltenKt.paper.extension.effect.particleOf
import de.moltenKt.paper.extension.effect.soundOf
import de.moltenKt.paper.extension.paper.offlinePlayer
import de.moltenKt.paper.extension.tasky.doSync
import de.moltenKt.paper.structure.command.completion.buildInterchangeStructure
import de.moltenKt.paper.structure.command.completion.component.CompletionAsset
import de.moltenKt.paper.structure.command.structured.StructuredPlayerInterchange
import de.moltenKt.paper.tool.display.message.Transmission.Level
import de.moltenKt.paper.tool.display.message.Transmission.Level.FAIL
import de.moltenKt.paper.tool.display.message.Transmission.Level.INFO
import de.moltenKt.paper.tool.effect.particle.ParticleType
import de.moltenKt.paper.tool.permission.Approval
import de.moltenKt.unfold.extension.Times
import de.moltenKt.unfold.extension.Title
import de.moltenKt.unfold.hover
import de.moltenKt.unfold.plus
import de.moltenKt.unfold.text
import home.HomeManager
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration.BOLD
import org.bukkit.Sound
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class HomeInterchange : StructuredPlayerInterchange("home", buildInterchangeStructure {

	branch {

		addContent("place")

		concludedExecution {

			HomeManager.playerHome(executor, executor.location)

			text {
				this + text("SUCCESSFULLY").style(Style.style(NamedTextColor.GREEN, BOLD))
				this + text(" set your home to your current location!").color(NamedTextColor.GRAY)
			}.notification(Level.APPLIED, executor).display()

		}

		requiredApproval(Approval("mtpah.home.place"))

		branch {

			addContent(CompletionAsset.OFFLINE_PLAYER_NAME)

			concludedExecution {
				val target = offlinePlayer(getInput(1))

				if (target.hasPlayedBefore()) {

					HomeManager.playerHome(target, executor.location)

					text {
						this + text("SUCCESSFULLY").style(Style.style(NamedTextColor.GREEN, BOLD))
						this + text(" set the home of ${target.name ?: "Unknown"} to your current location!").color(NamedTextColor.GRAY)
					}.notification(Level.APPLIED, executor).display()

				} else
					text {
						this + text("${target.name ?: "Unknown"} has never played before!").color(NamedTextColor.RED)
					}.notification(FAIL, executor).display()

			}

			requiredApproval(Approval("mtpah.home.placeOther"))

		}

	}

	branch {

		addContent("teleport")

		concludedExecution {
			val home = HomeManager.playerHome(executor)

			if (home != null) {

				executor.teleportAsync(home, COMMAND)
				soundOf(Sound.ENTITY_SHULKER_TELEPORT, pitch = .6).play(executor)
				particleOf(ParticleType.END_ROD).offset(3).location(executor.location).count(50).extra(.0).receivers(executor).spawn()
				executor.showTitle(Title("", "Welcome back to your home!", Times(Duration.ZERO, 3.seconds, 2.seconds)))

				text {
					this + text("SUCCESSFULLY").style(Style.style(NamedTextColor.GREEN, BOLD))
					this + text(" teleported you to your home!").color(NamedTextColor.GRAY)
				}.notification(Level.APPLIED, executor).display()

			} else {
				text {
					this + text("SORRY!").style(Style.style(NamedTextColor.RED, BOLD))
					this + text(" you don't have a home set!").color(NamedTextColor.GRAY)
				}.notification(FAIL, executor).display()
			}

		}

		requiredApproval(Approval("mtpah.home.teleport"))

		branch {

			addContent(CompletionAsset.OFFLINE_PLAYER_NAME)

			concludedExecution {
				val target = offlinePlayer(getInput(1))
				val targetHome = HomeManager.playerHome(target)

				if (targetHome != null) {

					doSync {

						executor.teleportAsync(targetHome, COMMAND)

						text {
							this + text("SUCCESSFULLY").style(Style.style(NamedTextColor.GREEN, BOLD))
							this + text(" teleported to ").color(NamedTextColor.GRAY)
							this + text(target.name ?: "Unknown").style(Style.style(NamedTextColor.YELLOW, BOLD))
							this + text("'s home!").color(NamedTextColor.GRAY)
						}.notification(Level.APPLIED, executor).display()

					}

				} else {
					text {
						this + text("SORRY!").style(Style.style(NamedTextColor.RED, BOLD))
						this + text(" ${target.name} doesn't have a home set!").color(NamedTextColor.GRAY)
					}.notification(FAIL, executor).display()
				}

			}

			requiredApproval(Approval("mtpah.home.teleportOther"))

		}

	}

	branch {

		addContent("delete")

		concludedExecution {
			val home = HomeManager.playerHome(executor)

			if (home != null) {

				HomeManager.playerHome(executor, null)

				text {
					this + text("SUCCESSFULLY").style(Style.style(NamedTextColor.GREEN, BOLD))
					this + text(" deleted your home!").color(NamedTextColor.GRAY)
				}.notification(Level.APPLIED, executor).display()

			} else {
				text {
					this + text("SORRY!").style(Style.style(NamedTextColor.RED, BOLD))
					this + text(" you don't have a home set!").color(NamedTextColor.GRAY)
				}.notification(FAIL, executor).display()
			}

		}

		requiredApproval(Approval("mtpah.home.delete"))

		branch {

			addContent(CompletionAsset.OFFLINE_PLAYER_NAME)

			concludedExecution {
				val target = offlinePlayer(getInput(1))
				val targetHome = HomeManager.playerHome(target)

				if (targetHome != null) {

					HomeManager.playerHome(target, null)

					text {
						this + text("SUCCESSFULLY").style(Style.style(NamedTextColor.GREEN, BOLD))
						this + text(" deleted ").color(NamedTextColor.GRAY)
						this + text(target.name ?: "Unknown").style(Style.style(NamedTextColor.YELLOW, BOLD))
						this + text("'s home!").color(NamedTextColor.GRAY)
					}.notification(Level.APPLIED, executor).display()

				} else {
					text {
						this + text("SORRY!").style(Style.style(NamedTextColor.RED, BOLD))
						this + text(" ${target.name} doesn't have a home set!").color(NamedTextColor.GRAY)
					}.notification(FAIL, executor).display()
				}

			}

			requiredApproval(Approval("mtpah.home.deleteOther"))

		}

	}

	concludedExecution {

		text {
			this + text("MakeThisPlaceAHome (MTPAH)") {
				style(Style.style(NamedTextColor.GOLD, BOLD))
				hover { text("Click to visit the plugin page!").color(NamedTextColor.GRAY) }
				clickEvent(ClickEvent.openUrl("https://modrinth.com/plugin/mtpah"))
			}
			this + text(" is developed by ").color(NamedTextColor.GRAY)
			this + text("@TheFruxz").style(Style.style(NamedTextColor.YELLOW, BOLD))
		}
			.notification(INFO, executor).display()

	}

})