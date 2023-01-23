/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.constants;

/**
 *
 * @author crramosl
 */
public class Catalog {

    public static class Case {

        public static class Status {

            public static final Integer ASSIGNED = 15;
            public static final Integer REJECTED_OP = 16;
            public static final Integer REJECTED = 17;
            public static final Integer ELABORATE_SCOPE = 18;
            public static final Integer SCOPE_REVIEW_JD = 19;
            public static final Integer PROGRAMMING_AUTHORIZATION = 133;
            public static final Integer SCOPE_CORRECTION = 181;
            public static final Integer PROGRAMMED = 182;
            public static final Integer PENDING_DOCUMENTATION = 183;
            public static final Integer PENDING_PUBLICATION = 184;
            public static final Integer PUBLISHED = 185;
            public static final Integer DOCUMENTED = 186;
            public static final Integer DELETED = 419;
            public static final Integer SUSPENDED = 1039;
            public static final Integer ULTIMATE_REJECTION = 1040;

        }

        public static class Type {

            public static final Integer SELECTIVE = 969;
            public static final Integer FIXED_POINTS = 971;
            public static final Integer CABINET = 972;
            public static final Integer EXTERNAL_REQUEST = 1041;

        }

        public static class Assigment {

            public static final Integer ACTIVE = 138;
            public static final Integer INACTIVE = 139;
        }

    }

    public static class Input {

        public static class Status {

            public static final Integer PENDING_PUBLICATION = 1081;
            public static final Integer DELETED = 1090;
            public static final Integer PUBLISHED = 177;
            public static final Integer PENDING_DOCUMENTATION = 178;
            public static final Integer REFUSED = 179;
            public static final Integer ASSIGNED_JU = 180;
            public static final Integer DISCONTINUED = 439;
            public static final Integer FINAL_REJECTION = 440;
            public static final Integer NO_PENDIG_CASES = 473;
        }

        public static class Type {

            public static final Integer FOREIGN_TRADE = 80;
            public static final Integer TRANSFER_PRICES = 81;
            public static final Integer INTERNAL_TAXES = 82;
            public static final Integer COMPLAINTS = 83;
            public static final Integer SELECTIVE = 1002;
            public static final Integer FIXED_POINTS = 1003;
            public static final Integer CABINET = 1004;

        }

        public static class ENTITY {

            public static final Integer PUBLIC_MINISTRY = 148;
            public static final Integer GENERAL_COMPTROLLER_OF_ACCOUNTS = 149;
            public static final Integer JUDICIAL_SYSTEM = 150;
            public static final Integer OTHERS = 151;
            public static final Integer AUDIT = 152;
            public static final Integer INFORMATION_CROSSING = 153;
            public static final Integer COMPLIANCE_WITH_TAX_OBLIGATIONS = 154;
            public static final Integer SALES_AND_OR_SERVICES_REPORT = 155;
        }
    }

    public static class Scope {

        public static class Status {

            public static final Integer CREATED = 0;
            public static final Integer PUBLISHED = 1;
            public static final Integer DOCUMENTATION_PENDING = 2;
            public static final Integer SCOPE_REVIEW_JU = 1091;
            public static final Integer SCOPE_REVIEW_JD = 1092;
            public static final Integer REJECTED = 1093;
            public static final Integer SCOPE_CORRECTION = 1094;
            public static final Integer REJECTED_OP = 1094;
            public static final Integer PROGRAMMED = 1096;
            public static final Integer PROGRAMMING_AUTHORIZATION = 1097;
        }

        public static class Type {

            public static final Integer SELECTIVE = 117;
            public static final Integer REQUEST = 118;
            public static final Integer TRANSFER_PRICES = 119;
            public static final Integer MASIVE = 120;
            public static final Integer PRESENCES = 973;
            public static final Integer COMPLAINTS = 974;
            public static final Integer FIXED_POINTS = 975;
            public static final Integer CABINET = 976;
        }
    }

    public static class FiscalPrograms {

        public static class Status {

            public static final Integer CORRECTION_REQUEST = 107;
            public static final Integer AUTHORIZED = 108;
            public static final Integer LOCKED = 109;
            public static final Integer REVISION = 110;
            public static final Integer APPROVAL = 111;
            public static final Integer UNLOCKED = 187;

        }

        public static class Binnacle {

            public static final Integer LOCKED = 411;
            public static final Integer UNLOCKED = 412;
            public static final Integer REVISION = 413;
            public static final Integer CORRECTION_REQUEST = 414;
            public static final Integer APROVAL = 415;
            public static final Integer AUTHORIZED = 416;
            public static final Integer UPDATE = 417;
            public static final Integer CREATE = 418;

        }

    }

    public static class Management {

        public static class Collaborators {

            public static class Status {

                public static final Integer DISCONTINUED = 1;
                public static final Integer WITH_LICENCE = 2;
                public static final Integer ENJO_HOLIDAYS = 3;
                public static final Integer ACTIVE = 4;
                public static final Integer IDLE = 5;
                public static final Integer REMOVED = 407;
            }
        }

        public static class ManagementUnits {

            public static class Status {

                public static final Integer ACTIVE = 136;
                public static final Integer IDLE = 137;
            }
        }

        public static class Profiles {

            public static class Status {

                public static final Integer ACTIVE = 159;
                public static final Integer IDLE = 160;
            }
        }

        public static class EmployeeProfileAssignmen {

            public static class Status {

                public static final Integer ACTIVE = 161;
                public static final Integer IDLE = 162;
            }
        }

        public static class Workgroup {

            public static class Status {

                public static final Integer ACTIVE = 163;
                public static final Integer IDLE = 164;
            }
        }

        public static class CollaboratorTransfer {

            public static class Type {

                public static final Integer TEMPORARY = 1068;
                public static final Integer DEFINITIVE = 1069;
            }
        }

        public static class Member {

            public static class Status {

                public static final Integer ACTIVE = 170;
                public static final Integer IDLE = 171;
            }
        }
       
    }
            public static class Complaint {
            
            public static class Status {
                public static final Integer APPLY = 957;
                public static final Integer REJECTED = 958; 
                public static final String ENTRY = "991";
                public static final Integer ASSIGNED = 956;
            }
        }
}
