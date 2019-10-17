class DqNetworkDashboard extends HTMLElement {
    static getTemplate() {
        return `   
        <style>
        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 12px;
        }
        
        table thead tr td {
            text-align: center;
            color: #fff;
            background-color: #20425a;
            border: 1px solid #ddd;
        }
        
        table tbody tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        
        table tbody tr td:first-child {
            color: #fff;
            background-color: #20425a;
        }
        
        table tbody tr td {
            text-align: right;
            border: 1px solid #ddd;
        }
        
        td {
            color: #000;
            padding: 3px 7px 3px 7px;
            font-size: 20px;
        }
        
        td.overall {
            font-size: 28px;
            font-weight: bold;
        }
        
        td:empty,
        th:empty {
            border: 0;
            background: transparent;
        }
        
        td.fail {
            color: #C00;
            font-weight: bold;
        }
        </style>
        
        <table>
        <thead>
            <tr>
                <td></td>
                <td colspan="2">Plausibility</td>
                <td colspan="2">Conformance</td>
                <td colspan="2">Completeness</td>
                <td></td>
            </tr>
            <tr>
                <td>Data Source</td>
                <td>Validation</td>
                <td>Verification</td>
                <td>Validation</td>
                <td>Verification</td>
                <td>Validation</td>
                <td>Verification</td>
                <td>Total</td>
            </tr>
        </thead>
        <tbody>
            {{#each sources}}
            <tr>
                <td>{{this.Name}}</td>
                <td>{{this.Validation.Plausibility.PercentPass}}</td>
                <td>{{this.Verification.Plausibility.PercentPass}}</td>
                <td>{{this.Validation.Conformance.PercentPass}}</td>
                <td>{{this.Verification.Conformance.PercentPass}}</td>
                <td>{{this.Validation.Completeness.PercentPass}}</td>
                <td>{{this.Verification.Completeness.PercentPass}}</td>
                <td>{{this.Total.Total.PercentPass}}</td>
            </tr>
            {{/each}}
        </tbody>
        </table>        
        `;
    }

    connectedCallback() {
        this.root = this.attachShadow({ mode: 'open' });
        this.render();
    }

    static get observedAttributes() {
        return ['data-results'];
    }

    attributeChangedCallback(name, oldValue, newValue) {
        if (this.root && oldValue !== newValue) {
            this.render();
        }
    }

    get results() {
        return JSON.parse(this.getAttribute('data-results'));
    }

    render() {
        if (!this.results)
            return;
        var derivedResults = [];
        for (var i = 0; i < this.results.length; i++) {
            // Verification Plausibility
            var VerificationPlausibilityPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Verification"
                    && c.CATEGORY == "Plausibility"
            ).length;

            var VerificationPlausibilityFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Verification"
                    && c.CATEGORY == "Plausibility"
            ).length;

            var VerificationPlausibilityTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Verification"
                    && c.CATEGORY == "Plausibility"
            ).length;

            var VerificationPlausibilityPercentPass = VerificationPlausibilityTotal == 0 ? "-" : Math.round(VerificationPlausibilityPass / VerificationPlausibilityTotal * 100) + "%";

            // Verification Conformance
            var VerificationConformancePass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Verification"
                    && c.CATEGORY == "Conformance"
            ).length;

            var VerificationConformanceFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Verification"
                    && c.CATEGORY == "Conformance"
            ).length;

            var VerificationConformanceTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Verification"
                    && c.CATEGORY == "Conformance"
            ).length;

            var VerificationConformancePercentPass = VerificationConformanceTotal == 0 ? "-" : Math.round(VerificationConformancePass / VerificationConformanceTotal * 100) + "%";

            // Verification Completeness
            var VerificationCompletenessPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Verification"
                    && c.CATEGORY == "Completeness"
            ).length;

            var VerificationCompletenessFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Verification"
                    && c.CATEGORY == "Completeness"
            ).length;

            var VerificationCompletenessTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Verification"
                    && c.CATEGORY == "Completeness"
            ).length;

            var VerificationCompletenessPercentPass = VerificationCompletenessTotal == 0 ? "-" : Math.round(VerificationCompletenessPass / VerificationCompletenessTotal * 100) + "%";

            // Verification Totals
            var VerificationPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Verification"
            ).length;

            var VerificationFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Verification"
            ).length;

            var VerificationTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Verification"
            ).length;

            var VerificationPercentPass = VerificationTotal == 0 ? "-" : Math.round(VerificationPass / VerificationTotal * 100) + "%";

            // Validation Plausibility
            var ValidationPlausibilityPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Validation"
                    && c.CATEGORY == "Plausibility"
            ).length;

            var ValidationPlausibilityFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Validation"
                    && c.CATEGORY == "Plausibility"
            ).length;

            var ValidationPlausibilityTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Validation"
                    && c.CATEGORY == "Plausibility"
            ).length;

            var ValidationPlausibilityPercentPass = ValidationPlausibilityTotal == 0 ? "-" : Math.round(ValidationPlausibilityPass / ValidationPlausibilityTotal * 100) + "%";

            // Validation Conformance
            var ValidationConformancePass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Validation"
                    && c.CATEGORY == "Conformance"
            ).length;

            var ValidationConformanceFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Validation"
                    && c.CATEGORY == "Conformance"
            ).length;

            var ValidationConformanceTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Validation"
                    && c.CATEGORY == "Conformance"
            ).length;

            var ValidationConformancePercentPass = ValidationConformanceTotal == 0 ? "-" : Math.round(ValidationConformancePass / ValidationConformanceTotal * 100) + "%";

            // Validation Completeness
            var ValidationCompletenessPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Validation"
                    && c.CATEGORY == "Completeness"
            ).length;

            var ValidationCompletenessFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Validation"
                    && c.CATEGORY == "Completeness"
            ).length;

            var ValidationCompletenessTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Validation"
                    && c.CATEGORY == "Completeness"
            ).length;

            var ValidationCompletenessPercentPass = ValidationCompletenessTotal == 0 ? "-" : Math.round(ValidationCompletenessPass / ValidationCompletenessTotal * 100) + "%";

            // Validation
            var ValidationPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CONTEXT == "Validation"
            ).length;

            var ValidationFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CONTEXT == "Validation"
            ).length;

            var ValidationTotal = this.results[i].CheckResults.filter(
                c => c.CONTEXT == "Validation"
            ).length;

            var ValidationPercentPass = ValidationTotal == 0 ? "-" : Math.round(ValidationPass / ValidationTotal * 100) + "%";

            // Plausibility
            var PlausibilityPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0 &&
                    c.CATEGORY == "Plausibility"
            ).length;

            var PlausibilityFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1 &&
                    c.CATEGORY == "Plausibility"
            ).length;

            var PlausibilityTotal = this.results[i].CheckResults.filter(
                c => c.CATEGORY == "Plausibility"
            ).length;

            var PlausibilityPercentPass = PlausibilityTotal == 0 ? "-" : Math.round(PlausibilityPass / PlausibilityTotal * 100) + "%";

            // Conformance
            var ConformancePass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0
                    && c.CATEGORY == "Conformance"
            ).length;

            var ConformanceFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1
                    && c.CATEGORY == "Conformance"
            ).length;

            var ConformanceTotal = this.results[i].CheckResults.filter(
                c => c.CATEGORY == "Conformance"
            ).length;

            var ConformancePercentPass = ConformanceTotal == 0 ? "-" : Math.round(ConformancePass / ConformanceTotal * 100) + "%";

            // Completeness
            var CompletenessPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0
                    && c.CATEGORY == "Completeness"
            ).length;

            var CompletenessFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1
                    && c.CATEGORY == "Completeness"
            ).length;

            var CompletenessTotal = this.results[i].CheckResults.filter(
                c => c.CATEGORY == "Completeness"
            ).length;

            var CompletenessPercentPass = CompletenessTotal == 0 ? "-" : Math.round(CompletenessPass / CompletenessTotal * 100) + "%";

            // All
            var AllPass = this.results[i].CheckResults.filter(
                c => c.FAILED == 0
            ).length;

            var AllFail = this.results[i].CheckResults.filter(
                c => c.FAILED == 1
            ).length;

            var AllTotal = this.results[i].CheckResults.length;

            var AllPercentPass = AllTotal == 0 ? "-" : Math.round(AllPass / AllTotal * 100) + "%";

            var derivedResult = {
                "Name": this.results[i].Metadata[0].CDM_SOURCE_NAME,
                "Verification": {
                    "Plausibility": {
                        "Pass": VerificationPlausibilityPass,
                        "Fail": VerificationPlausibilityFail,
                        "Total": VerificationPlausibilityTotal,
                        "PercentPass": VerificationPlausibilityPercentPass
                    },
                    "Conformance": {
                        "Pass": VerificationConformancePass,
                        "Fail": VerificationConformanceFail,
                        "Total": VerificationConformanceTotal,
                        "PercentPass": VerificationConformancePercentPass
                    },
                    "Completeness": {
                        "Pass": VerificationCompletenessPass,
                        "Fail": VerificationCompletenessFail,
                        "Total": VerificationCompletenessTotal,
                        "PercentPass": VerificationCompletenessPercentPass
                    },
                    "Total": {
                        "Pass": VerificationPass,
                        "Fail": VerificationFail,
                        "Total": VerificationTotal,
                        "PercentPass": VerificationPercentPass
                    }
                },
                "Validation": {
                    "Plausibility": {
                        "Pass": ValidationPlausibilityPass,
                        "Fail": ValidationPlausibilityFail,
                        "Total": ValidationPlausibilityTotal,
                        "PercentPass": ValidationPlausibilityPercentPass
                    },
                    "Conformance": {
                        "Pass": ValidationConformancePass,
                        "Fail": ValidationConformanceFail,
                        "Total": ValidationConformanceTotal,
                        "PercentPass": ValidationConformancePercentPass
                    },
                    "Completeness": {
                        "Pass": ValidationCompletenessPass,
                        "Fail": ValidationCompletenessFail,
                        "Total": ValidationCompletenessTotal,
                        "PercentPass": ValidationCompletenessPercentPass
                    },
                    "Total": {
                        "Pass": ValidationPass,
                        "Fail": ValidationFail,
                        "Total": ValidationTotal,
                        "PercentPass": ValidationPercentPass
                    }
                },
                "Total": {
                    "Plausibility": {
                        "Pass": PlausibilityPass,
                        "Fail": PlausibilityFail,
                        "Total": PlausibilityTotal,
                        "PercentPass": PlausibilityPercentPass
                    },
                    "Conformance": {
                        "Pass": ConformancePass,
                        "Fail": ConformanceFail,
                        "Total": ConformanceTotal,
                        "PercentPass": ConformancePercentPass
                    },
                    "Completeness": {
                        "Pass": CompletenessPass,
                        "Fail": CompletenessFail,
                        "Total": CompletenessTotal,
                        "PercentPass": CompletenessPercentPass
                    },
                    "Total": {
                        "Pass": AllPass,
                        "Fail": AllFail,
                        "Total": AllTotal,
                        "PercentPass": AllPercentPass
                    }
                }
            }
            derivedResults.push(derivedResult);
        }
        var data = { sources: derivedResults };

        var hbTemplate = Handlebars.compile(DqNetworkDashboard.getTemplate());
        var html = hbTemplate(data);
        this.root.innerHTML = html;
    }
}

customElements.define('dq-network-dashboard', DqNetworkDashboard);
