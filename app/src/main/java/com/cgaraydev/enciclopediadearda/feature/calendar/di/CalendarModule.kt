package com.cgaraydev.enciclopediadearda.feature.calendar.di

import com.cgaraydev.enciclopediadearda.feature.calendar.data.DemoCalendarRepository
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarModule {

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        implementation: DemoCalendarRepository
    ): CalendarRepository
}