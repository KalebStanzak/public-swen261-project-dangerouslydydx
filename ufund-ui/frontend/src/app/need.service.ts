import { Injectable } from '@angular/core';
import { Need } from './need'
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NeedService {

  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl)
    .pipe(
      tap(_ => this.log('fetched needs')),
      catchError(this.handleError<Need[]>('getNeeds', []))
    );
  }
  
  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * 
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   * */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
      
      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a NeedService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`NeedService: ${message}`);
  }

  constructor(
    private messageService: MessageService,
    private http: HttpClient
  ) { }

  private needsUrl = 'http://localhost:2006/needs';  // URL to needs REST API

  getNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.get<Need>(url).
    pipe(
      tap(_ => this.log(`fetched need id=${id}`)),
      catchError(this.handleError<Need>(`getNeed id=${id}`))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /** PUT: update the need on the server */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions).pipe(
      tap(_ => this.log(`updated need id=${need.id}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  /** POST: add a new need to the server */
  addNeed(need: Need): Observable<Need> {
    if (!this.searchNeeds(need.name) == null){
      catchError(this.handleError<Need>('addNeed'));
    }
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => this.log(`added need w/ id=${newNeed.id}`)),
      catchError(this.handleError<Need>('addNeed'))
    );
  }

  /** DELETE: delete the need from the server */
  deleteNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;
    
    return this.http.delete<Need>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted need id=${id}`)),
      catchError(this.handleError<Need>('deleteNeed'))
    );
  }

  /* GET needs whose name contains search term */
  searchNeeds(term: string): Observable<Need[]> {
    if (!term.trim()) {
      // if not search term, return empty need array.
      return of([]);
    }
    
    return this.http.get<Need[]>(`${this.needsUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
        this.log(`found needs matching "${term}"`) :
        this.log(`no needs matching "${term}"`)),
        catchError(this.handleError<Need[]>('searchNeeds', []))
      );
  }

}
