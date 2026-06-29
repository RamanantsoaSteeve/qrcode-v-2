export interface ProductResponse {
    price: number;
    id: number;
    name: string;
    createdat: string;
    user_id: number;
    currency: string;
}

export interface qrcodeResponseWithId {
    qrcode: string;
    id: number;
}


export interface qrcodeResponse {
    qrcode: string;
    id: number;
}


export interface ProductState {
    qrcode?: string,
    name?: string,
    price?: number;
    id?: number;
    currencySymbol?: string;
};

