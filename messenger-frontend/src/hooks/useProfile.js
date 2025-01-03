import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
    fetchUserProfile,
    updateUserProfile,
    uploadAvatar,
    deleteAvatar,
    clearUpdateSuccess,
    clearError,
    updateUserPassword,
    deleteProfile,
    setUserOnlineStatus
} from '../redux/slices/profileSlice';
import { webSocketService } from './api/websocket';

export const useProfile = () => {
    const dispatch = useDispatch();
    const {
        userProfile,
        loading,
        error,
        avatarUrl,
        updateSuccess
    } = useSelector(state => state.profile);

    useEffect(() => {
        dispatch(fetchUserProfile());

        const token = localStorage.getItem('token');
        if (token) {
            webSocketService.connect(token).then(() => {
                webSocketService.send('/app/user.connect', {});
            });
        }

        const handleUnload = () => {
            webSocketService.send('/app/user.disconnect', {});
        };

        window.addEventListener('unload', handleUnload);

        return () => {
            webSocketService.disconnect();
            window.removeEventListener('unload', handleUnload);
        };
    }, [dispatch]);

    const updateProfile = async (userData) => {
        try {
            await dispatch(updateUserProfile(userData)).unwrap();
            return true;
        } catch (error) {
            return false;
        }
    };

    const updatePassword = async (passwordData) => {
        try {
            await dispatch(updateUserPassword(passwordData)).unwrap();
            return true;
        } catch (error) {
            return false;
        }
    };

    const handleAvatarUpload = async (file) => {
        try {
            await dispatch(uploadAvatar(file)).unwrap();
            return true;
        } catch (error) {
            return false;
        }
    };

    const handleAvatarDelete = async () => {
        try {
            await dispatch(deleteAvatar()).unwrap();
            return true;
        } catch (error) {
            return false;
        }
    };

    const handleClearUpdateSuccess = () => {
        dispatch(clearUpdateSuccess());
    };

    const handleClearError = () => {
        dispatch(clearError());
    };

    const deleteUserProfile = async () => {
        try {
            await dispatch(deleteProfile()).unwrap();
            return true;
        } catch (error) {
            console.error('Failed to delete profile:', error);
            throw error;
        }
    };

    return {
        userProfile,
        loading,
        error,
        avatarUrl,
        updateSuccess,
        updateProfile,
        updatePassword,
        handleAvatarUpload,
        handleAvatarDelete,
        handleClearUpdateSuccess,
        handleClearError,
        deleteUserProfile
    };
};

export default useProfile;