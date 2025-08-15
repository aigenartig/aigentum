import { orderServiceApi } from './apiClient';
import { Order } from '../types';

export const getCart = async (userId: string): Promise<Order> => {
  const response = await orderServiceApi.get(`/orders/cart/${userId}`);
  return response.data;
};

export const addItemToCart = async (userId: string, productId: string, quantity: number): Promise<Order> => {
  const response = await orderServiceApi.post(`/orders/cart/${userId}/items`, { productId, quantity });
  return response.data;
};

export const updateCartItem = async (userId: string, itemId: string, quantity: number): Promise<Order> => {
  const response = await orderServiceApi.put(`/orders/cart/${userId}/items/${itemId}`, { quantity });
  return response.data;
};

export const deleteCartItem = async (userId: string, itemId: string): Promise<void> => {
  await orderServiceApi.delete(`/orders/cart/${userId}/items/${itemId}`);
};

export const getOrdersForUser = async (userId: string): Promise<Order[]> => {
  const response = await orderServiceApi.get(`/orders/user/${userId}`);
  return response.data;
};

export const createOrderFromCart = async (userId: string): Promise<Order> => {
  const response = await orderServiceApi.post(`/orders/cart/${userId}/checkout`);
  return response.data;
};
