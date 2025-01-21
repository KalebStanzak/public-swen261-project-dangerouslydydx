import { Need } from "./need";
export interface Basket {
    id: number;
    name: string;
    funds: number;
    needs: Need[];
}
