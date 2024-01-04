import { Component, OnInit } from '@angular/core';
import { AnayzeResultCacheService } from '../anayze-result-cache.service';
import {
  faClock,
  faPause,
  faCog,
  faAngry,
  faSmile,
  faLock,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Chart } from 'chart.js/auto';
import * as d3 from 'd3';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-dump-result',
  templateUrl: './dump-result.component.html',
  styleUrls: ['./dump-result.component.css'],
})
export class DumpResultComponent implements OnInit {
  constructor(
    private analyzedresult: AnayzeResultCacheService,
    private http: HttpClient,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.getThreadCountByState();
    this.getDaemonAndNonDaemonThreadsCount();
    this.getsamePoolThreads();
    this.setStateCountChartData();
    this.isDeadlockOccure();
    this.getCPUConsumingThreads();
    this.getGarbageCollectionThreads();
  }

  dataSource: any;
  api = environment.apiUrl;
  clock = faClock;
  pause = faPause;
  gear = faCog;
  lock = faLock;
  daemon = faAngry;
  happy = faSmile;
  garbage = faTrash;
  panelOpenState = false;

  runnableCount: any = 0;
  timedWaitingCount: any = 0;
  waitingCount: any = 0;
  blockedCount: any = 0;
  totalThreads: any = 0;
  daemonThreads: any = 0;
  nonDaemonThreads: any = 0;
  circleData: any = null;
  deadlockRelation: any = [];

  state: string = '';
  poolName: string = '';
  isDaemon: boolean = false;
  deadLockResult: any = null;
  cpuConsumingThreads: any;
  garbageCollectionThreads: any;

  isDeadlockOccure(): void {
    this.http.get(`${this.api}detectDeadLock`).subscribe((result) => {
      this.deadLockResult = result;
      this.deadlockRelation = this.deadLockResult.deadLockDetails;
      this.getDeadlockResult(this.deadLockResult.threadsInCycle);
    });
  }

  getCPUConsumingThreads(): void {
    this.http.get(`${this.api}highCPUConsumingThreads`).subscribe((result) => {
      this.cpuConsumingThreads = result;
    });
  }

  getThreadCountByState(): void {
    this.analyzedresult.getAnalysisResult().subscribe((result) => {
      result.threadDumps.forEach((threadDump: any) => {
        const state = threadDump.state.toLowerCase();
        state == 'runnable'
          ? this.runnableCount++
          : state == 'timed_waiting'
          ? this.timedWaitingCount++
          : state == 'waiting'
          ? this.waitingCount++
          : state == 'blocked'
          ? this.blockedCount++
          : null;
      });
      this.totalThreads =
        this.runnableCount + this.timedWaitingCount + this.waitingCount;
    });
  }

  getDaemonAndNonDaemonThreadsCount(): void {
    this.analyzedresult.getAnalysisResult().subscribe((result) => {
      result.threadDumps.forEach((threadDump: any) => {
        threadDump.daemon ? this.daemonThreads++ : this.nonDaemonThreads++;
      });
    });
  }

  getGarbageCollectionThreads(): void {
    this.http.get(`${this.api}garbageCollecters`).subscribe((result) => {
      this.garbageCollectionThreads = result;
    });
  }

  getsamePoolThreads(): void {
    this.http.get(`${this.api}samePoolThreads`).subscribe((result) => {
      this.dataSource = result;
      console.log(this.dataSource);
    });
  }

  setThreadDetailsBasedOnState(state: string): void {
    this.state = state;
    this.http
      .post(`${this.api}threadsByState`, { state: state })
      .subscribe((result) => {
        this.analyzedresult.setAnalysisFilteredResult(result);
        this.router.navigate(['/threads-details']);
      });
  }

  setThreadDetailsBasedOnPool(pooName: string): void {
    this.poolName = pooName;
    this.http
      .post(`${this.api}threadsByPool`, { poolName: pooName })
      .subscribe((result) => {
        this.analyzedresult.setAnalysisFilteredResult(result);
        this.router.navigate(['/threads-details']);
      });
  }

  setThreadDetailsOfDaemonOrNonDaemonThreads(isDaemon: boolean): void {
    this.isDaemon = isDaemon;
    this.http
      .post(`${this.api}threadsByDaemonOrNonDaemon`, { isDaemon: isDaemon })
      .subscribe((result) => {
        this.analyzedresult.setAnalysisFilteredResult(result);
        this.router.navigate(['/threads-details']);
      });
  }

  setStateCountChartData(): void {
    const statelabels = ['RUNNABLE', 'TIMED_WAITING', 'WAITING'];
    const statedata = [
      this.runnableCount,
      this.timedWaitingCount,
      this.waitingCount,
    ];
    const daemonLables = ['Daemon', 'Non-Daemon'];
    const daemonData = [this.daemonThreads, this.nonDaemonThreads];
    if (this.blockedCount > 0) {
      statelabels.push('BLOCKED');
      statedata.push(this.blockedCount);
    }
    new Chart('stateCountChart', {
      type: 'bar',
      data: {
        labels: statelabels,
        datasets: [
          {
            data: statedata,
            backgroundColor: ['#39a7ff', '#ffcc00', '#00ff00', '#FF0000'],
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            display: false,
          },
        },
      },
    });
    new Chart('daemonNonDaemonCountChart', {
      type: 'bar',
      data: {
        labels: daemonLables,
        datasets: [
          {
            data: daemonData,
            backgroundColor: ['#39a7ff', '#FF0000'],
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            display: false,
          },
        },
      },
    });
  }

  setGarbageCollectionThreads(): void {
    this.http.get(`${this.api}garbageCollecters`).subscribe((result) => {
      this.analyzedresult.setAnalysisFilteredResult(result);
      this.router.navigate(['/threads-details']);
    });
  }

  getDeadlockResult(deadlockData: any[]): void {
    console.log(deadlockData);

    this.circleData = deadlockData.map((d: any, i: number) => {
      return {
        label: d.pool,
        x: 450 + 350 * Math.cos((i / deadlockData.length) * 2 * Math.PI),
        y: 450 + 350 * Math.sin((i / deadlockData.length) * 2 * Math.PI),
      };
    });
    console.log(this.circleData);
    this.drawCircularGraph(this.circleData);
  }

  drawCircularGraph(circleData: any[]): void {
    const svg = d3
      .select('.mapping-section')
      .append('svg')
      .attr('width', 1000)
      .attr('height', 850);

    // Append circles
    const circles = svg
      .selectAll('circle')
      .data(circleData)
      .enter()
      .append('circle')
      .attr('cx', (d) => d.x)
      .attr('cy', (d) => d.y)
      .attr('r', 40)
      .style('fill', '#FFEED0');

    // Append text labels
    const text = svg
      .selectAll('text')
      .data(circleData)
      .enter()
      .append('text')
      .attr('x', (d) => d.x - 50)
      .attr('y', (d) => d.y)
      .text((d) => d.label)
      .style('text-anchor', 'middle')
      .style('alignment-baseline', 'middle')
      .style('fill', '#000');

    // Append lines to connect circles
    const lines = svg
      .selectAll('line')
      .data(circleData)
      .enter()
      .append('line')
      .attr('x1', (d, i) => d.x)
      .attr('y1', (d, i) => d.y)
      .attr('x2', (d, i) => circleData[(i + 1) % circleData.length].x)
      .attr('y2', (d, i) => circleData[(i + 1) % circleData.length].y)
      .style('stroke', '#39a7ff')
      .attr('marker-end', 'url(#arrowhead)');

    const lineLabels = svg
      .selectAll('.line-label')
      .data(circleData)
      .enter()
      .append('text')
      .attr('class', 'line-label')
      .attr(
        'x',
        (d, i) => (d.x + circleData[(i + 1) % circleData.length].x) / 2
      )
      .attr(
        'y',
        (d, i) => (d.y + circleData[(i + 1) % circleData.length].y) / 2
      )
      .text((d, i) => `${circleData[i].label} is waiting to lock resource, which is held by ${circleData[i+1] == undefined ? circleData[0].label : circleData[i+1].label}` )
      .style('text-anchor', 'middle')
      .style('alignment-baseline', 'middle')
      .style('fill', '#000');
      
    svg
      .append('defs')
      .append('marker')
      .attr('id', 'arrowhead')
      .attr('refX', 100)
      .attr('refY', 6)
      .attr('markerWidth', 100)
      .attr('markerHeight', 100)
      .attr('orient', 'auto')
      .append('path')
      .attr('d', 'M 0 0 L 12 6 L 0 12 Z'); // Path for arrowhead
  }
}
