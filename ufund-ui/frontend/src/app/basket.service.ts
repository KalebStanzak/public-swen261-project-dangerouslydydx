import { Injectable } from '@angular/core';
import { Basket } from './basket';
import { Need } from './need'
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service'
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BasketService {
  private basketUrl = 'http://localhost:2006/api/user/basket';
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private http: HttpClient
  ) {}

  private handleError<T>(operation = 'operation', result?:T) {
    return (error:any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  getBasket(): Observable<Basket> {
    console.log("get basket called by basketService")
    return this.http.get<Basket>(this.basketUrl).pipe(
      tap((basket) => console.log('Fetched basket:', basket)),
      catchError(this.handleError<Basket>('getBasket'))
    );
  }

  addNeedToBasket(id:number, need: Need): Observable<Need> {
    console.log("basket service addNeedToBasket called");
    console.log("adding need with id: ", id, need);
    return this.http.post<Need>(this.basketUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => console.log('added need to basket w/ id:' + id)),
      catchError(this.handleError<Need>('addNeedToBasket'))
    );
  }

  removeNeedFromBasket(id: number): Observable<Need> {
    const url = `${this.basketUrl}/delete/${id}`;
    return this.http.delete<Need>(url, this.httpOptions).pipe(
      tap(() => console.log(`removed need with id=${id} from basket`)),
      catchError(this.handleError<Need>('deleteNeedFromBasket'))
    );
  }  

  checkoutBasket(id:number): Observable<string> {
    const url = '${this.basketUrl}/${id}/checkout';
    return this.http.post<string>(url, {}, this.httpOptions).pipe(
      tap(_ => console.log('basket checked out')),
      catchError(this.handleError<string>('checkoutBasket'))
    );
  }

}
