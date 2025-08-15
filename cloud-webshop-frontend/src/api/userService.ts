import { userServiceApi } from './apiClient';
import type { User } from '../types';

export const getUserProfile = async (userId: string): Promise<User> => {
  const response = await userServiceApi.get(`/users/${userId}`);
  return response.data;
};

export const updateUserProfile = async (user: User): Promise<User> => {
  const response = await userServiceApi.put(`/users/${user.id}`, user);
  return response.data;
};

export const deleteUserAccount = async (userId: string): Promise<void> => {
  await userServiceApi.delete(`/users/${userId}`);
};
