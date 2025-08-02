import { configureStore } from '@reduxjs/toolkit';
import policyReducer from './policySlice';
import memberReducer from './memberSlice';

export const store = configureStore({
    reducer: { policy: policyReducer, member: memberReducer },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
