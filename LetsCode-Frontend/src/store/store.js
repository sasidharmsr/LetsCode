import { configureStore } from '@reduxjs/toolkit'

import authReducer from './slices/authSlice'
import  topicsSlice  from './slices/topicsSlice'
import  homeSlice  from './slices/HomePageSlice'
import profileSlice from './slices/profileSlice'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    topics:topicsSlice,
    home:homeSlice,
    profile:profileSlice
  },
})