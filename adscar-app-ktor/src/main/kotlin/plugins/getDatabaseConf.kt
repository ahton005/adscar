package plugins // ktlint-disable filename

import AdRepoInMemory
import RepoAdCassandra
import configs.CassandraConfig
import configs.ConfigPaths
import io.ktor.server.application.Application
import repo.IAdRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class AdDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: AdDbType): IAdRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
//        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        "cassandra", "nosql", "cass" -> initCassandra()
        // "arcade", "arcadedb", "graphdb", "gremlin" -> initGremliln()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

// private fun Application.initPostgres(): IAdRepository {
//    val config = PostgresConfig(environment.config)
//    return RepoAdSQL(
//        properties = SqlProperties(
//            url = config.url,
//            user = config.user,
//            password = config.password,
//            schema = config.schema,
//        )
//    )
// }

private fun Application.initCassandra(): IAdRepository {
    val config = CassandraConfig(environment.config)
    return RepoAdCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
        testing = config.testing
    )
}

private fun Application.initInMemory(): IAdRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return AdRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}
