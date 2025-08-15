export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  DELETED = 'DELETED',
}

export enum OrderStatus {
  CART = 'CART',
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED',
}

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
