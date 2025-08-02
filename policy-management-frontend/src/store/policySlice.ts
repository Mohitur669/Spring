import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { policyService } from '../services/policyService';
import type { Policy } from '../types/policy';

export const fetchPolicies = createAsyncThunk('policy/fetchAll',
    async () => await policyService.getAllPolicies());

const slice = createSlice({
    name: 'policy',
    initialState: { items: [] as Policy[], loading: false },
    reducers: {},
    extraReducers: builder => {
        builder
            .addCase(fetchPolicies.pending, s => { s.loading = true; })
            .addCase(fetchPolicies.fulfilled, (s, a) => {
                s.items = a.payload; s.loading = false;
            });
    },
});

export default slice.reducer;
