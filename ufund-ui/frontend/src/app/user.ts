import { Basket } from './basket';
import { Need } from './need';
export interface User {
    userID: number;
    username: string;
    permissions: number;
    basket: Basket;
    favorites: Need[];

}
