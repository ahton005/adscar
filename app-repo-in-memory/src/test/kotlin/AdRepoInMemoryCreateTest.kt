import org.junit.Test

class AdRepoInMemoryCreateTest : RepoAdCreateTest() {
    override val repo = AdRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )

    @Test
    override fun createSuccess() {
        super.createSuccess()
    }
}
