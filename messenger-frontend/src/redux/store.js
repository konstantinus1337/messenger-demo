import { configureStore } from '@reduxjs/toolkit';
import authReducer from './slices/authSlice';
import friendsReducer from './slices/friendsSlice';
import profileReducer from './slices/profileSlice';
import chatsReducer from './slices/chatsSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        friends: friendsReducer,
        profile: profileReducer,
        chats: chatsReducer,
    }
});