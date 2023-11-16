import org.junit.Test
import repo.IAdRepository

class AdRepoInMemoryReadTest : RepoAdReadTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )

    @Test
    override fun readSuccess() {
        super.readSuccess()
    }

    @Test
    override fun readNotFound() {
        super.readNotFound()
    }
}
