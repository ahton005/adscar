import models.InnerAd
import models.InnerAdId
import models.InnerVisibility
import permissions.InnerPrincipalModel
import permissions.InnerPrincipalRelations

fun InnerAd.resolveRelationsTo(principal: InnerPrincipalModel): Set<InnerPrincipalRelations> = setOfNotNull(
    InnerPrincipalRelations.NONE,
    InnerPrincipalRelations.NEW.takeIf { id == InnerAdId.NONE },
    InnerPrincipalRelations.OWN.takeIf { principal.id == ownerId },
    InnerPrincipalRelations.MODERATABLE.takeIf { visibility != InnerVisibility.VISIBLE_TO_OWNER },
    InnerPrincipalRelations.PUBLIC.takeIf { visibility == InnerVisibility.VISIBLE_PUBLIC }
)
