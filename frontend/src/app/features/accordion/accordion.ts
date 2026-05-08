import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-accordion',
  imports: [],
  templateUrl: './accordion.html',
  styleUrl: './accordion.scss',
})
export class Accordion {

    @Input() title: string=" prova titolo";
    @Input() id: string="id prova"
    @Input() description: string= '';

}
