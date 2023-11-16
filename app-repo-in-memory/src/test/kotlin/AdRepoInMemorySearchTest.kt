import org.junit.Test
import repo.IAdRepository

class AdRepoInMemorySearchTest : RepoAdSearchTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )

    @Test
    override fun searchOwner() {
        super.searchOwner()
    }
}
