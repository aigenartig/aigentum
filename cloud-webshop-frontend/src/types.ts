export type UserStatus = 'ACTIVE' | 'INACTIVE' | 'DELETED';

export type OrderStatus =
  | 'CART'
  | 'PENDING'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED';

export interface User {
  id: string; // UUID
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  status: UserStatus;
  createdAt: string; // TIMESTAMPTZ
  updatedAt: string; // TIMESTAMPTZ
}

export interface OrderItem {
  id: string; // UUID
  productId: string; // UUID
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  createdAt: string; // TIMESTAMPTZ
  updatedAt: string; // TIMESTAMPTZ
}

export interface Order {
  id: string; // UUID
  orderNumber: string;
  userId: string; // UUID
  status: OrderStatus;
  totalAmount: number;
  currency: string;
  shippingAddress: string;
  billingAddress: string;
  items: OrderItem[];
  createdAt: string; // TIMESTAMPTZ
  updatedAt: string; // TIMESTAMPTZ
}
