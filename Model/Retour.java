public class Retour {
  /**
   * 
   * @param returnState
   * @param loan
   * @param validatedBy
   */
    public Retour(String returnState, Emprunt loan, Bibliothecaire validatedBy) {
		this.returnState = returnState;
		this.loan = loan;
		this.validatedBy = validatedBy;
    }

    final String returnState;
    final Emprunt loan;
    final Bibliothecaire validatedBy;

	public String getReturnState() {
		return (this.returnState);
	}
	
	public Emprunt getReference() {
		return (this.loan);
	}

	public Bibliothecaire getEmploye() {
		return (this.validatedBy);
	}
}
