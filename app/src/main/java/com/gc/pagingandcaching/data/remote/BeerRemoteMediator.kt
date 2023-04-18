package com.gc.pagingandcaching.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.gc.pagingandcaching.data.local.BeerDatabase
import com.gc.pagingandcaching.data.local.BeerEntity
import com.gc.pagingandcaching.data.mappers.toBeerEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDb: BeerDatabase,
    private val beerApi: BeerApi
) : RemoteMediator<Int, BeerEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {

        return try {
            val loadKey = when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null){
                        1
                    }else{
                        (lastItem.id/state.config.pageSize) + 1
                    }
                }
            }
            delay(2000L)
            val beers = beerApi.getBeers(page = loadKey, pageCount = state.config.pageSize)

            beerDb.withTransaction {
                if (loadType == LoadType.REFRESH){
                    beerDb.dao.cleanAll()
                }
                val beerEntitys = beers.map { it.toBeerEntity() }
                beerDb.dao.upsertAll(beerEntitys)
            }

            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty()
            )

        }catch (e : IOException){
            MediatorResult.Error(e)
        }catch (e : HttpException){
            MediatorResult.Error(e)
        }
    }

}