import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.Update
import model.AdCassandraDTO
import repo.DbAdFilterRequest
import java.util.concurrent.CompletionStage

@Dao
interface AdCassandraDAO {
    @Insert
    fun create(dto: AdCassandraDTO): CompletionStage<Unit>

    @Select
    fun read(id: String): CompletionStage<AdCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    fun update(dto: AdCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock = :prevLock", entityClass = [AdCassandraDTO::class])
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @QueryProvider(providerClass = AdCassandraSearchProvider::class, entityHelpers = [AdCassandraDTO::class])
    fun search(filter: DbAdFilterRequest): CompletionStage<Collection<AdCassandraDTO>>
}
