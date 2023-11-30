package helpers // ktlint-disable filename

import AdRepoInMemory
import AdRepoStub
import AppSettings
import MkplCorSettings
import repo.IAdRepository

fun testSettings(repo: IAdRepository? = null) = AppSettings(
    corSettings = MkplCorSettings(
        repoStub = AdRepoStub(),
        repoTest = repo ?: AdRepoInMemory(),
        repoProd = repo ?: AdRepoInMemory()
    )
)
